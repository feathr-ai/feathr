---
layout: default
title: Storing secrets in Azure Key Vault
parent: How-to Guides
---

## Storing secrets in Azure Key Vault.

As part of the Azure Resource Manager (ARM) deployment template, Azure Key Vault gets created and some of the secrets are pre-saved in it, for example Redis Connection String.


If you want to add additional secrets to key vault, you could do so through Azure Key Vault's [CLI guide](https://learn.microsoft.com/en-us/azure/key-vault/general/manage-with-cli2#adding-a-key-secret-or-certificate-to-the-key-vault) or through [Azure Portal](https://learn.microsoft.com/en-us/azure/key-vault/secrets/quick-create-portal) or by running the following command in the [Cloud Shell](https://shell.azure.com/bash)

```bash
az keyvault secret set --vault-name "YOUR_KV" --name "SECRET_NAME" --value "SECRET_VALUE"
```