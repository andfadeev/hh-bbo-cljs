(ns hh-bbo-cljs.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [re-frisk.core :refer [enable-re-frisk!]]
              [hh-bbo-cljs.events]
              [hh-bbo-cljs.subs]
              [hh-bbo-cljs.views :as views]
              [hh-bbo-cljs.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/service-purchase-block]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:service-purchase-history/load-data])
  (dev-setup)
  (mount-root))
