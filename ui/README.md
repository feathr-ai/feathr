# Feathr UI

This is Feathr web ui frontend code

## Prerequisites
Install the latest version of [NodeJS](https://nodejs.org/en/) LTS v14.x or v16.x. Make sure to use npm no later than 6.x. Run **node --version** and **npm --version** to verify installed versions.

## Build and run locally

Each command in this section should be run from the root directory of the repository.

Open terminal, go to root of this repository and run following commands.

```bash
cd ui
npm install
npm start
```

This should launch [http://localhost:3000/](http://localhost:3000/) on your web browser. The page will reload when you make code changes and save.

## Deploying

For deployment, run `npm run build` and upload `build/` to your server. Docker image based deployment is coming soon.

## Lint

To lint typescript code files, sim run:
```bash
npm run lint:fix
```

This command will Automatically fix all problems that can be fixed, and list the rest problems requires manual fix. 
Linting rules are configured in [.eslintrc](.eslintrc) file. 

## Project Structure

```
src/
  api         // rest client
  components  // shared react components
  models      // api data model, view model, etc
  pages       // a view on the page, can be routed by url path
  router      // url path and page mapping
```

