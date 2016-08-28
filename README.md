# europe-travel-map

A visualization showing the EU countries and cities I've been to with d3.js and Reagent.

See: [http://henryzhu.me/map](http://henryzhu.me/map)

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build

```
lein clean
lein cljsbuild once min
```
