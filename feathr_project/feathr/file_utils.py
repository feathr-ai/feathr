import os

def write_to_file(content: str, file_name: str):
    path_to_script = os.path.dirname(os.path.abspath(__file__))
    my_filename = os.path.join(path_to_script, file_name)

    with open(my_filename, "w") as handle:
        print(content, file=handle)