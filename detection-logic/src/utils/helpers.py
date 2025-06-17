"""
Helper utilities
Author: Alims-Repo
Date: 2025-06-17
"""

import asyncio
import time
from typing import Any, Callable, TypeVar, List
import functools

T = TypeVar('T')


def async_timer(func: Callable) -> Callable:
    """Decorator to time async functions"""
    @functools.wraps(func)
    async def wrapper(*args, **kwargs):
        start_time = time.time()
        result = await func(*args, **kwargs)
        execution_time = time.time() - start_time
        
        # Log if function takes too long
        if execution_time > 0.1:  # 100ms threshold
            print(f"⚠️  {func.__name__} took {execution_time:.3f}s")
        
        return result
    return wrapper


def timer(func: Callable) -> Callable:
    """Decorator to time sync functions"""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start_time = time.time()
        result = func(*args, **kwargs)
        execution_time = time.time() - start_time
        
        if execution_time > 0.05:  # 50ms threshold for sync functions
            print(f"⚠️  {func.__name__} took {execution_time:.3f}s")
        
        return result
    return wrapper


class PerformanceTracker:
    """Track performance metrics"""
    
    def __init__(self, max_samples: int = 100):
        self.max_samples = max_samples
        self.times = []
        self.counts = []
    
    def add_sample(self, execution_time: float, count: int = 1):
        """Add a performance sample"""
        self.times.append(execution_time)
        self.counts.append(count)
        
        # Keep only recent samples
        if len(self.times) > self.max_samples:
            self.times.pop(0)
            self.counts.pop(0)
    
    @property
    def average_time(self) -> float:
        """Get average execution time"""
        return sum(self.times) / len(self.times) if self.times else 0.0
    
    @property
    def average_count(self) -> float:
        """Get average count"""
        return sum(self.counts) / len(self.counts) if self.counts else 0.0
    
    @property
    def fps(self) -> float:
        """Calculate FPS based on recent samples"""
        if not self.times:
            return 0.0
        return 1.0 / self.average_time if self.average_time > 0 else 0.0
    
    def get_stats(self) -> dict:
        """Get comprehensive statistics"""
        if not self.times:
            return {
                "samples": 0,
                "avg_time": 0.0,
                "avg_count": 0.0,
                "fps": 0.0,
                "min_time": 0.0,
                "max_time": 0.0
            }
        
        return {
            "samples": len(self.times),
            "avg_time": self.average_time,
            "avg_count": self.average_count,
            "fps": self.fps,
            "min_time": min(self.times),
            "max_time": max(self.times),
            "recent_times": self.times[-10:]  # Last 10 samples
        }


async def run_with_timeout(coro, timeout: float, default_value=None):
    """Run coroutine with timeout"""
    try:
        return await asyncio.wait_for(coro, timeout=timeout)
    except asyncio.TimeoutError:
        print(f"⚠️  Operation timed out after {timeout}s")
        return default_value


def ensure_list(value: Any) -> List:
    """Ensure value is a list"""
    if isinstance(value, list):
        return value
    return [value]


def clamp(value: float, min_val: float, max_val: float) -> float:
    """Clamp value between min and max"""
    return max(min_val, min(value, max_val))


def format_bytes(bytes_value: int) -> str:
    """Format bytes in human readable format"""
    for unit in ['B', 'KB', 'MB', 'GB']:
        if bytes_value < 1024.0:
            return f"{bytes_value:.1f}{unit}"
        bytes_value /= 1024.0
    return f"{bytes_value:.1f}TB"