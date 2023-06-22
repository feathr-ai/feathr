from IPython.core.getipython import get_ipython
import re


def create_new_cell(contents):
    shell = get_ipython()
    payload = dict(
        source="set_next_input",
        text=contents,
        replace=False,
    )
    shell.payload_manager.write_payload(payload, single=False)


def extract_code_from_string(input_str, lang="python"):
    """Extract the code block for a given language"""
    pattern = rf"```({lang}|\s*)\n(.*?)\n```"
    # Use the re.findall() function to extract the code block
    match = re.search(pattern, input_str, re.DOTALL)
    # Check if any code block was found
    if match:
        return match.group(2)
    else:
        return ""
