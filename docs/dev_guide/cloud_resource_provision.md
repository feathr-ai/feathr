---
layout: default
title: Cloud Resource Provisioning
parent: Feathr Developer Guides
---
# Cloud Resource Provisioning

To simplify the cloud environment setup, we give end users an Azure template (with a button to have a simplified experience) so they can provision resources easily. From a high level point of view, it's a JSON file that Azure knows how to parse, and you specify the corresponding resources in the JSON file and Azure will validate and provision it for you.

The JSON file is located [here](../how-to-guides/azure_resource_provision.json), and developers can paste it to the [Azure Template Manager](https://ms.portal.azure.com/#blade/HubsExtension/TemplateEditorBladeV2/template/) to run the template with updates they want to have.

The template contains a lot Azure specific terms, and more details can be found here for [Azure Templates](https://docs.microsoft.com/en-us/azure/azure-resource-manager/templates/overview). 