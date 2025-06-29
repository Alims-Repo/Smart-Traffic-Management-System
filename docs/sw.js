// Service Worker for TrafficFlow AI
// Version: 2.1.0
// Last Updated: 2025-06-29 06:40:49 UTC

const CACHE_NAME = 'trafficflow-ai-v2.1.0';
const STATIC_CACHE = 'trafficflow-static-v2.1.0';
const DYNAMIC_CACHE = 'trafficflow-dynamic-v2.1.0';

// Files to cache immediately
const STATIC_FILES = [
    '/',
    '/index.html',
    '/manifest.json',
    'https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&family=Space+Grotesk:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500;600&display=swap',
    'https://cdnjs.cloudflare.com/ajax/libs/aos/2.3.4/aos.css',
    'https://cdnjs.cloudflare.com/ajax/libs/aos/2.3.4/aos.js',
    '/assets/images/icon-192.png',
    '/assets/images/icon-512.png'
];

// Dynamic cache patterns
const CACHE_PATTERNS = [
    /\.(?:png|jpg|jpeg|svg|gif|webp|avif)$/,
    /\.(?:js|css)$/,
    /^https:\/\/fonts\.googleapis\.com/,
    /^https:\/\/fonts\.gstatic\.com/,
    /^https:\/\/cdnjs\.cloudflare\.com/
];

// Install event - cache static files
self.addEventListener('install', (event) => {
    console.log('SW: Install event');

    event.waitUntil(
        caches.open(STATIC_CACHE)
            .then((cache) => {
                console.log('SW: Caching static files');
                return cache.addAll(STATIC_FILES);
            })
            .then(() => {
                return self.skipWaiting();
            })
            .catch((error) => {
                console.error('SW: Install failed', error);
            })
    );
});

// Activate event - cleanup old caches
self.addEventListener('activate', (event) => {
    console.log('SW: Activate event');

    event.waitUntil(
        caches.keys()
            .then((cacheNames) => {
                return Promise.all(
                    cacheNames.map((cacheName) => {
                        if (cacheName !== STATIC_CACHE &&
                            cacheName !== DYNAMIC_CACHE &&
                            cacheName !== CACHE_NAME) {
                            console.log('SW: Deleting old cache', cacheName);
                            return caches.delete(cacheName);
                        }
                    })
                );
            })
            .then(() => {
                return self.clients.claim();
            })
    );
});

// Fetch event - serve cached files or fetch from network
self.addEventListener('fetch', (event) => {
    const { request } = event;
    const url = new URL(request.url);

    // Handle navigation requests
    if (request.mode === 'navigate') {
        event.respondWith(
            caches.match('/')
                .then((response) => {
                    return response || fetch(request);
                })
                .catch(() => {
                    return caches.match('/');
                })
        );
        return;
    }

    // Handle other requests
    event.respondWith(
        caches.match(request)
            .then((response) => {
                // Return cached version if available
                if (response) {
                    console.log('SW: Serving from cache', request.url);
                    return response;
                }

                // Fetch from network
                return fetch(request)
                    .then((fetchResponse) => {
                        // Check if we should cache this response
                        if (shouldCache(request, fetchResponse)) {
                            const responseClone = fetchResponse.clone();

                            caches.open(DYNAMIC_CACHE)
                                .then((cache) => {
                                    cache.put(request, responseClone);
                                });
                        }

                        return fetchResponse;
                    })
                    .catch((error) => {
                        console.error('SW: Fetch failed', error);

                        // Return offline fallback for images
                        if (request.destination === 'image') {
                            return new Response(
                                '<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 200 200"><rect width="200" height="200" fill="#f3f4f6"/><text x="100" y="100" text-anchor="middle" fill="#9ca3af" font-family="sans-serif">Offline</text></svg>',
                                { headers: { 'Content-Type': 'image/svg+xml' } }
                            );
                        }

                        throw error;
                    });
            })
    );
});

// Helper function to determine if response should be cached
function shouldCache(request, response) {
    // Only cache successful responses
    if (!response || response.status !== 200 || response.type !== 'basic') {
        return false;
    }

    // Don't cache if explicitly told not to
    if (response.headers.get('cache-control') === 'no-cache') {
        return false;
    }

    // Check if URL matches cache patterns
    return CACHE_PATTERNS.some(pattern => pattern.test(request.url));
}

// Background sync for offline actions
self.addEventListener('sync', (event) => {
    console.log('SW: Background sync', event.tag);

    if (event.tag === 'background-sync') {
        event.waitUntil(
            // Perform background tasks here
            console.log('SW: Performing background sync')
        );
    }
});

// Push notifications
self.addEventListener('push', (event) => {
    console.log('SW: Push received', event);

    const options = {
        body: event.data ? event.data.text() : 'New update available!',
        icon: '/assets/images/icon-192.png',
        badge: '/assets/images/badge-72.png',
        vibrate: [200, 100, 200],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: 1
        },
        actions: [
            {
                action: 'explore',
                title: 'View Details',
                icon: '/assets/images/checkmark.png'
            },
            {
                action: 'close',
                title: 'Close',
                icon: '/assets/images/xmark.png'
            }
        ]
    };

    event.waitUntil(
        self.registration.showNotification('TrafficFlow AI', options)
    );
});

// Handle notification clicks
self.addEventListener('notificationclick', (event) => {
    console.log('SW: Notification click', event);

    event.notification.close();

    if (event.action === 'explore') {
        event.waitUntil(
            clients.openWindow('/')
        );
    }
});

// Message handling for communication with main thread
self.addEventListener('message', (event) => {
    console.log('SW: Message received', event.data);

    if (event.data && event.data.type === 'SKIP_WAITING') {
        self.skipWaiting();
    }

    if (event.data && event.data.type === 'GET_VERSION') {
        event.ports[0].postMessage({ version: CACHE_NAME });
    }
});

// Periodic background sync
self.addEventListener('periodicsync', (event) => {
    console.log('SW: Periodic sync', event.tag);

    if (event.tag === 'update-data') {
        event.waitUntil(
            // Update cached data periodically
            updateCachedData()
        );
    }
});

async function updateCachedData() {
    try {
        const cache = await caches.open(DYNAMIC_CACHE);
        const response = await fetch('/api/v1/analytics/current');

        if (response.ok) {
            await cache.put('/api/v1/analytics/current', response);
            console.log('SW: Updated cached analytics data');
        }
    } catch (error) {
        console.error('SW: Failed to update cached data', error);
    }
}

// Share target handling
self.addEventListener('share', (event) => {
    console.log('SW: Share received', event);

    event.waitUntil(
        clients.openWindow('/share?data=' + encodeURIComponent(JSON.stringify(event.data)))
    );
});

// Error handling
self.addEventListener('error', (event) => {
    console.error('SW: Error', event.error);
});

self.addEventListener('unhandledrejection', (event) => {
    console.error('SW: Unhandled rejection', event.reason);
});