// ================================
// MAIN JAVASCRIPT FUNCTIONALITY
// ================================

class TrafficFlowApp {
    constructor() {
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.initializeComponents();
        this.setupPerformanceOptimizations();
    }

    setupEventListeners() {
        // Theme toggle
        document.getElementById('themeToggle')?.addEventListener('click', this.toggleTheme.bind(this));

        // Mobile menu
        document.getElementById('mobileMenuBtn')?.addEventListener('click', this.toggleMobileMenu.bind(this));

        // Smooth scrolling for navigation links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', this.handleSmoothScroll.bind(this));
        });

        // Copy to clipboard buttons
        document.querySelectorAll('.copy-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.copyToClipboard(e.target));
        });

        // Window events
        window.addEventListener('scroll', this.throttle(this.handleScroll.bind(this), 16));
        window.addEventListener('resize', this.debounce(this.handleResize.bind(this), 250));
        window.addEventListener('load', this.handlePageLoad.bind(this));
    }

    initializeComponents() {
        this.createParticles();
        this.setupScrollAnimations();
        this.initializeCounters();
        this.simulateDashboardData();
        this.loadSavedTheme();
    }

    setupPerformanceOptimizations() {
        this.setupLazyLoading();
        this.preloadCriticalResources();
        this.registerServiceWorker();
    }

    // Theme Management
    toggleTheme() {
        const body = document.body;
        const themeIcon = document.getElementById('themeIcon');
        const isDark = body.getAttribute('data-theme') === 'dark';

        if (isDark) {
            body.removeAttribute('data-theme');
            themeIcon.textContent = '🌙';
            localStorage.setItem('theme', 'light');
        } else {
            body.setAttribute('data-theme', 'dark');
            themeIcon.textContent = '☀️';
            localStorage.setItem('theme', 'dark');
        }

        // Dispatch custom event for theme change
        window.dispatchEvent(new CustomEvent('themeChanged', {
            detail: { theme: isDark ? 'light' : 'dark' }
        }));
    }

    loadSavedTheme() {
        const savedTheme = localStorage.getItem('theme');
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        const themeIcon = document.getElementById('themeIcon');

        if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
            document.body.setAttribute('data-theme', 'dark');
            if (themeIcon) themeIcon.textContent = '☀️';
        }
    }

    // Mobile Menu
    toggleMobileMenu() {
        const navLinks = document.querySelector('.nav-links');
        const mobileMenuBtn = document.getElementById('mobileMenuBtn');

        navLinks?.classList.toggle('mobile-open');
        mobileMenuBtn?.classList.toggle('active');
    }

    // Smooth Scrolling
    handleSmoothScroll(e) {
        e.preventDefault();
        const target = document.querySelector(e.target.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    }

    // Scroll Handling
    handleScroll() {
        this.updateScrollProgress();
        this.updateHeaderOnScroll();
    }

    updateScrollProgress() {
        const winScroll = document.body.scrollTop || document.documentElement.scrollTop;
        const height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const scrolled = (winScroll / height) * 100;
        const progressIndicator = document.getElementById('progressIndicator');
        if (progressIndicator) {
            progressIndicator.style.width = scrolled + '%';
        }
    }

    updateHeaderOnScroll() {
        const header = document.getElementById('header');
        const currentScrollY = window.scrollY;

        if (!header) return;

        if (currentScrollY > 100) {
            header.style.background = 'rgba(255, 255, 255, 0.95)';
            header.style.backdropFilter = 'blur(20px)';
        } else {
            header.style.background = 'var(--glass-bg)';
            header.style.backdropFilter = 'var(--glass-backdrop)';
        }
    }

    // Resize Handling
    handleResize() {
        // Handle responsive adjustments
        this.updateParticlesOnResize();
    }

    // Page Load
    handlePageLoad() {
        document.body.style.opacity = '1';
        this.startCounterAnimations();
    }

    // Particles System
    createParticles() {
        const particlesContainer = document.getElementById('heroParticles');
        if (!particlesContainer) return;

        const particleCount = this.getParticleCount();

        for (let i = 0; i < particleCount; i++) {
            const particle = this.createParticle();
            particlesContainer.appendChild(particle);
        }
    }

    createParticle() {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.top = Math.random() * 100 + '%';
        particle.style.animationDelay = Math.random() * 6 + 's';
        particle.style.animationDuration = (Math.random() * 3 + 3) + 's';
        return particle;
    }

    getParticleCount() {
        const width = window.innerWidth;
        if (width < 768) return 20;
        if (width < 1024) return 35;
        return 50;
    }

    updateParticlesOnResize() {
        const particlesContainer = document.getElementById('heroParticles');
        if (!particlesContainer) return;

        const currentCount = particlesContainer.children.length;
        const targetCount = this.getParticleCount();

        if (currentCount !== targetCount) {
            particlesContainer.innerHTML = '';
            this.createParticles();
        }
    }

    // Scroll Animations
    setupScrollAnimations() {
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('in-view');

                    // Trigger specific animations for certain elements
                    if (entry.target.classList.contains('hero-stat')) {
                        this.animateCounter(entry.target);
                    }
                }
            });
        }, observerOptions);

        document.querySelectorAll('.fade-in, .hero-stat').forEach(el => {
            observer.observe(el);
        });
    }

    // Counter Animations
    initializeCounters() {
        const counterElements = {
            'accuracyStat': { target: 95, suffix: '%' },
            'fpsStat': { target: 30, suffix: '+' },
            'uptimeStat': { target: 99.9, suffix: '%' },
            'responseStat': { target: 200, suffix: 'ms' }
        };

        Object.entries(counterElements).forEach(([id, config]) => {
            const element = document.getElementById(id);
            if (element) {
                element.dataset.target = config.target;
                element.dataset.suffix = config.suffix;
            }
        });
    }

    startCounterAnimations() {
        document.querySelectorAll('.hero-stat h3').forEach(el => {
            this.animateCounter(el);
        });
    }

    animateCounter(element, duration = 2000) {
        const targetValue = parseFloat(element.dataset.target || element.textContent.replace(/[^\d.]/g, ''));
        const suffix = element.dataset.suffix || '';
        let startValue = 0;
        const increment = targetValue / (duration / 16);

        const timer = setInterval(() => {
            startValue += increment;
            if (startValue >= targetValue) {
                element.textContent = targetValue + suffix;
                clearInterval(timer);
            } else {
                const currentValue = suffix === '%' && targetValue < 100 ?
                    startValue.toFixed(1) : Math.floor(startValue);
                element.textContent = currentValue + suffix;
            }
        }, 16);
    }

    // Dashboard Simulation
    simulateDashboardData() {
        const updateInterval = 3000;

        setInterval(() => {
            this.updateDashboardMetrics();
        }, updateInterval);
    }

    updateDashboardMetrics() {
        const updates = {
            'vehicleCount': () => (1245 + Math.floor(Math.random() * 100) - 50).toLocaleString(),
            'avgSpeed': () => (42.3 + (Math.random() * 10) - 5).toFixed(1) + ' km/h',
            'congestionLevel': () => ['Low', 'Medium', 'High'][Math.floor(Math.random() * 3)],
            'incidents': () => Math.floor(Math.random() * 5) + ' Active'
        };

        Object.entries(updates).forEach(([id, updateFn]) => {
            const element = document.getElementById(id);
            if (element) {
                element.textContent = updateFn();
                element.classList.add('updated');
                setTimeout(() => element.classList.remove('updated'), 500);
            }
        });
    }

    // Copy to Clipboard
    async copyToClipboard(button) {
        const codeBlock = button.parentElement.nextElementSibling;
        const text = codeBlock.textContent.trim();

        try {
            await navigator.clipboard.writeText(text);
            this.showCopyFeedback(button, 'Copied!');
        } catch (err) {
            // Fallback for older browsers
            this.fallbackCopyToClipboard(text);
            this.showCopyFeedback(button, 'Copied!');
        }
    }

    fallbackCopyToClipboard(text) {
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed';
        textArea.style.left = '-999999px';
        textArea.style.top = '-999999px';
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
    }

    showCopyFeedback(button, message) {
        const originalText = button.textContent;
        const originalBackground = button.style.background;

        button.textContent = message;
        button.style.background = 'var(--success)';

        setTimeout(() => {
            button.textContent = originalText;
            button.style.background = originalBackground;
        }, 2000);
    }

    // Performance Optimizations
    setupLazyLoading() {
        if ('IntersectionObserver' in window) {
            const imageObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const img = entry.target;
                        img.src = img.dataset.src;
                        img.classList.remove('lazy');
                        imageObserver.unobserve(img);
                    }
                });
            });

            document.querySelectorAll('img[data-src]').forEach(img => {
                imageObserver.observe(img);
            });
        }
    }

    preloadCriticalResources() {
        const criticalResources = [
            'https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap'
        ];

        criticalResources.forEach(resource => {
            const link = document.createElement('link');
            link.rel = 'preload';
            link.href = resource;
            link.as = 'style';
            document.head.appendChild(link);
        });
    }

    registerServiceWorker() {
        if ('serviceWorker' in navigator) {
            window.addEventListener('load', () => {
                navigator.serviceWorker.register('/sw.js')
                    .then(registration => {
                        console.log('Service Worker registered:', registration);
                    })
                    .catch(error => {
                        console.log('Service Worker registration failed:', error);
                    });
            });
        }
    }

    // Utility Functions
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    throttle(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    }

    // Error Handling
    handleError(error, context = 'Unknown') {
        console.error(`Error in ${context}:`, error);

        // Could send to error tracking service
        if (window.gtag) {
            gtag('event', 'exception', {
                'description': error.toString(),
                'fatal': false
            });
        }
    }
}

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    try {
        window.trafficFlowApp = new TrafficFlowApp();
    } catch (error) {
        console.error('Failed to initialize TrafficFlow App:', error);
    }
});

// Global error handler
window.addEventListener('error', (event) => {
    console.error('Global error:', event.error);
});

window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason);
});