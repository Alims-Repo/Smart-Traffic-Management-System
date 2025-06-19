"""
Main entry point for M1 Vehicle Detection Server
Author: Alims-Repo
Date: 2025-06-17
"""

import sys
import os
from pathlib import Path

# Add project root to Python path
project_root = Path(__file__).parent.parent
sys.path.insert(0, str(project_root))

from src.utils.logging_config import setup_logging
from src.server.app import VehicleDetectionServer


def main():
    """Main entry point"""
    # Setup logging
    logger = setup_logging()
    logger.info("🚀 Vehicle Detection Server")

    try:
        # Create and run server
        server = VehicleDetectionServer()
        server.run()
    except KeyboardInterrupt:
        logger.info("👋 Server stopped by user")
    except Exception as e:
        logger.error(f"❌ Server failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()