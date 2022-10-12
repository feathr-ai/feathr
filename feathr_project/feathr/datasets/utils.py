"""Dataset utilities
"""
import logging
import math
from pathlib import Path
import requests

from tqdm import tqdm


log = logging.getLogger(__name__)


def maybe_download(src_url: str, dst_path: str, expected_bytes=None) -> bool:
    """Check if the file exists and download if needed.

    Refs:
        https://github.com/microsoft/recommenders/blob/main/recommenders/datasets/download_utils.py

    Args:


    Returns:


    """
    dst = Path(dst_path)

    # Check file if exists. If not, download and return true. Else, return false.
    if dst.is_file():
        log.info(f"File {dst_path} already exists")
        return False

    # Check dir if exists. If not, create one
    dst.parent.mkdir(parents=True, exist_ok=True)

    r = requests.get(src_url, stream=True)
    if r.status_code == 200:
        log.info(f"Downloading {src_url}")
        total_size = int(r.headers.get("content-length", 0))
        block_size = 1024
        num_iterables = math.ceil(total_size / block_size)
        with open(dst_path, "wb") as file:
            for data in tqdm(
                r.iter_content(block_size),
                total=num_iterables,
                unit="KB",
                unit_scale=True,
            ):
                file.write(data)

        # Verify the file size
        if expected_bytes is not None and expected_bytes != dst.stat().st_size:
            # Delete the file since the size is not the same as the expected one.
            dst.unlink()
            raise IOError(f"Failed to verify {dst_path}. Maybe interrupted while downloading?")
        else:
            return True

    else:
        print("wtf")
        r.raise_for_status()
        # If not HTTPError yet still cannot download
        raise Exception(f"Problem downloading {src_url}")
