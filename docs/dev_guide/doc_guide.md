---
layout: default
title: Documentation Guideline
parent: Developer Guides
---

# Documentation Guideline

We prefer simplicity and currently use GitHub page to host the Feathr user documentation. Those documentation will be built automatically by GitHub pipelines in the main branch.

## Hierarchy

In order for your docs to be rendered properly in the documentation hierarchy, Feathr developers need to add the section below at the top of each documentation. The `title` section will be what end user actually see in the side bar, and the `parent` section represents the parent page of the current page for linking purpose.

```
---
layout: default
title: Feathr Documentation Guideline
parent: Developer Guides
---
```

## Emojis

Here's a link to all the emojis available in README files: [Emoji Cheat Sheet](https://github.com/ikatyang/emoji-cheat-sheet/blob/master/README.md). If you want to find a good emoji, you can use [this website](https://emojicombos.com/).

## Code Snippets

Please use \`\`\` instead ~~~ when writing a code block in Jupyter Notebook because `pytest-check-links` which is used to check broken links is unable to parse ~~~ correctly.

Example error when ~~~ is used in Jupyter Notebooks:
```
pytest --check-links --check-links-cache --check-links-ignore "^https?:\/\/localhost(?:[:\/].+)?$" --check-links-ignore "^https?:\/\/pypi.org\/manage\/project\/feathr\/" docs/samples
============================================================ test session starts ============================================================
platform darwin -- Python 3.9.13, pytest-7.1.2, pluggy-1.0.0
rootdir: /Users/changyonglik/Desktop/opensource/feathr
plugins: anyio-3.6.1, check-links-0.7.1
collected 14 items / 3 errors

================================================================== ERRORS ===================================================================
_________________________________________ ERROR collecting docs/samples/fraud_detection_demo.ipynb __________________________________________
.venvs/feathr-dev/lib/python3.9/site-packages/pytest_check_links/plugin.py:180: in collect
    for item in self._items_from_notebook():
.venvs/feathr-dev/lib/python3.9/site-packages/pytest_check_links/plugin.py:167: in _items_from_notebook
    html = MarkdownWithMath(renderer=renderer).render(cell.source)
.venvs/feathr-dev/lib/python3.9/site-packages/nbconvert/filters/markdown_mistune.py:125: in render
    return self.parse(s)
.venvs/feathr-dev/lib/python3.9/site-packages/mistune/markdown.py:50: in parse
    result = self.block.render(tokens, self.inline, state)
.venvs/feathr-dev/lib/python3.9/site-packages/mistune/block_parser.py:274: in render
    return inline.renderer.finalize(data)
.venvs/feathr-dev/lib/python3.9/site-packages/mistune/renderers.py:220: in finalize
    return ''.join(data)
.venvs/feathr-dev/lib/python3.9/site-packages/mistune/block_parser.py:291: in _iter_render
    yield method(children, *params)
.venvs/feathr-dev/lib/python3.9/site-packages/nbconvert/filters/markdown_mistune.py:154: in block_code
    lang = info.strip().split(None, 1)[0]
E   IndexError: list index out of range
========================================================== short test summary info ==========================================================
ERROR docs/samples/fraud_detection_demo.ipynb - IndexError: list index out of range
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Interrupted: 1 errors during collection !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
============================================================= 1 errors in 8.20s =============================================================
```
