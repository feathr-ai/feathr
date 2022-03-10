import os
from pathlib import Path

def write_to_file(content: str, full_file_name: str):
    """Write content to a file.
    Attributes:
        content: content to write into the file
        full_file_name: full file path
    """
    path_to_script = os.path.dirname(os.path.abspath(__file__))
    my_filename = os.path.join(path_to_script, full_file_name)
    file_name_start = full_file_name.rfind("/")
    if file_name_start > 0:
        dir_name = my_filename[:file_name_start]
        Path(dir_name).mkdir(parents=True, exist_ok=True)
    with open(my_filename, "w") as handle:
        print(content, file=handle)