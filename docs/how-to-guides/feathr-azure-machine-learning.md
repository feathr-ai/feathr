---
layout: default
title: Using Feathr in Azure Machine Learning
parent: How-to Guides
---

# Using Feathr in Azure Machine Learning

Feathr has native integration with Azure Machine Learning (AML). However due to a few known issues, users have to do a little bit more on using Feathr in Azure Machine Learning.

## Installing Feathr in Azure Machine Learning

1. Switch python version. By default, Azure Machine Learning Notebooks uses an old Python version (3.6) which is not supported by Feathr. You should use the latest Python version in Azure Machine Learning. Switch it by using the button below:
   ![Switch Python Version](../images/aml-environment-switch.png)
2. Install Feathr using the following command. Instead using `!pip install feathr` in Azure Machine Learning, you should use the following command to install Feathr, to make sure that Feathr is available in the current active Python environment:

   ```python
   import pip
   pip.main(['install', 'feathr'])
   pip.main(['install', 'azure-identity>=1.8.0']) #fixing Azure Machine Learning authentication issue per https://stackoverflow.com/a/72262694/3193073
   ```

## Authentication in Azure Machine Learning

Azure Machine Learning has native integration to allow you authenticate. All the [Feathr sample notebooks](../samples/) will be able to seamlessly use the credentials that you have logged in.

When logged into Azure Machine Learning, you will see a prompt like this to ask you to login:

![Switch Python Version](../images/aml-authentication.png)

And after you have logged in, for all [Feathr sample notebooks](../samples/), simply remove those two lines because they are duplicated:

```bash
! pip install feathr azure-cli pandavro scikit-learn
! az login --use-device-code
```

And that's it! enjoy the rest of the capabilities that Azure Machine Learning brings to you, include distributed machine learning training and managed compute, etc.
