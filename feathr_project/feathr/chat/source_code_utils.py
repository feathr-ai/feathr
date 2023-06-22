import ast
import os
import re


def read_source_code_compact(module_path, source_file_or_dir_path):
    # Get a list of all Python files in the specified directory and its subdirectories
    # If it is a file, directly read the file.
    python_files = []
    if source_file_or_dir_path.endswith(".py"):
        python_files.append(source_file_or_dir_path)
    for root, dirs, files in os.walk(source_file_or_dir_path):
        for file in files:
            if file.endswith(".py"):
                python_files.append(os.path.join(root, file))

    # Concatenate the contents of all Python files into a single string
    concatenated_contents = ""
    for file_path in python_files:
        with open(file_path, "r") as f:
            # Parse the file into an abstract syntax tree (AST)
            tree = ast.parse(f.read())

            # Traverse the AST and remove function implementations
            for node in ast.walk(tree):
                remove_function_implementations(node)

            # Convert the AST back to Python code
            code = ast.unparse(tree)
            relative_path = os.path.relpath(file_path, module_path)
            concatenated_contents += "\n" + "In file: " + relative_path + "\n" + remove_comments(code)

    return concatenated_contents


def remove_comments(source):
    # Remove block comments
    source = re.sub(r'""".*?"""', "", source, flags=re.DOTALL)
    # Remove line comments
    source = re.sub(r"#.*?\n", "", source)
    # Remove empty lines
    source = re.sub(r"^\s*\n", "", source, flags=re.MULTILINE)
    return source


# Define a function to remove the function implementations
def remove_function_implementations(node):
    if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)):
        # Replace the function body with a single 'pass' statement
        node.body = [ast.Pass()]
    ast.fix_missing_locations(node)
