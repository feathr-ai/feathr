---
layout: default
title: Feathr Documentation Guideline
parent: Feathr Developer Guides
---

# Feathr Documentation Guideline

We prefer simplicy and currently use GitHub page to host the Feathr user documentation. Those documentation will be built automatically by GitHub pipelines in the main branch.

## Hierarchy 

In order for your docs to be rendenred properly in the documentation hierarchy, Feathr developers need to add the section below at the top of each documentation. The `title` section will be what end user actually see in the side bar, and the `parent` section represents the parent page of the current page for linking purpose.

```
---
layout: default
title: Feathr Documentation Guideline
parent: Feathr Developer Guides
---
```