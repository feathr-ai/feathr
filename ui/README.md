# Feathr UI

This is feathr web ui frontend code

## Build and run locally

Make sure you have the latest node LTS installed

Run the following commands in your terminal

```bash
npm i yarn -g
cd ui
yarn install
yarn start
```

Then open [http://localhost:3000/](http://localhost:3000/) on your web browser.

## Deploying

For deployment, run `yarn run build` and upload `build/` to your server.

# Docs

## Project Structure

```
ui/
  src/
    api         // rest client
    components  // shared react components
    models      // api dat model, view model, etc
    pages       // a view on the page
    router      // url path and page mapping
```

