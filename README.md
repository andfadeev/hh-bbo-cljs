# hh-bbo-cljs

hh-bbo-cljs demos

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

### Troubleshooting
Чтобы данные загрузились по ajax запросу, надо задизейблить CORS в браузере,
для хрома можно запустить так http://stackoverflow.com/questions/3102819/disable-same-origin-policy-in-chrome (проверял работет)
или так https://chrome.google.com/webstore/detail/allow-control-allow-origi/nlfbmbojpeacfghkpbjhddihlkkiljbi (не проверял)

Еще можно скачать plugin cursive (https://cursive-ide.com/) для idea и запускать из ide.

### Docs
- reagent - https://reagent-project.github.io/
- re-frame - https://github.com/Day8/re-frame
- figwheel - https://github.com/bhauman/lein-figwheel (https://www.youtube.com/watch?v=j-kj2qwJa_E)
 

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
