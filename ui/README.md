# Feathr UI

This is Feathr web ui frontend code

## Build and run locally

Make sure you have the latest node LTS installed

Run the following commands in your terminal

```bash
cd ui
npm install
npm start
```

Then open [http://localhost:3000/](http://localhost:3000/) on your web browser.

## Deploying

For deployment, run `npm run build` and upload `build/` to your server.

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

