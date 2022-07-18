# Feathr Feature Store UI

This directory hosts Feathr Feature Store UI code.

## Live Demo

Check out the latest Feathr Feature Store UI live demo [here](https://aka.ms/feathrdemo), use one of following accounts when you are prompted to login:

- A work or school organization account, includes Office 365 subscribers.
- Microsoft personal account, this means an account can access to Skype, Outlook.com, OneDrive, and Xbox LIVE.

## Development Getting Started

### Prerequisites

1. Install latest [Node](https://nodejs.org/en/) v16.x. Run `node --version` to verify installed Node versions.

### Build and run locally

Each command in this section should be run from the root directory of the repository.

Open terminal, go to root of this repository and run following commands.

```
cd ui
npm install
npm start
```

This should launch [http://localhost:3000](http://localhost:3000) on your web browser. The page will reload when you make code changes and save.

#### [Optional] Override configurations for local development

- **Point to a different backend endpoint**: by default, UI talks to live backend API at https://feathr-sql-registry.azurewebsites.net. To point to a custom backend API (e.g. running locally), create a .env.local in this directory and set REACT_APP_API_ENDPOINT, for example:

```
REACT_APP_API_ENDPOINT=http://localhost:8080
```

- **Use different authentication settings**: by default, UI authenticates with an Azure AD application with multiple tenants authentication enabled. To change to use a different Azure AD application, create a .env.local in this directory and set REACT_APP_AZURE_CLIENT_ID and REACT_APP_AZURE_TENANT_ID, for example:

```
REACT_APP_AZURE_CLIENT_ID=<REPLACE_WITH_YOUR_AZURE_CLIENT_ID>
REACT_APP_AZURE_TENANT_ID=<REPLACE_WITH_YOUR_TENANT_ID>
```

### Deploying

- For static file based deployment, run `npm run build` and upload `build/` to your server.
- For docker image based deployment, run `docker -t <image_name> .` to build image and push to your container registry.

### Linting & Formatting

#### Linting

To lint ts and tsx code, run:

```
npm run lint:fix
```

This command will Automatically fix all problems that can be fixed, and list the rest problems requires manual fix.
Linting rules are configured in [.eslintrc](.eslintrc) file. [Read More](https://eslint.org/docs/rules/).

#### Formatting

[Prettier](https://prettier.io/) is being used to format the UI code. Currently, the [default options](https://prettier.io/docs/en/options.html) are being used.

To format the code, run:

```
npm run format
```

### Project Structure

```
src/
  api         // rest client
  components  // shared react components
  models      // api data model, view model, etc
  pages       // a view on the page, can be routed by url path
  router      // url path and page mapping
```
