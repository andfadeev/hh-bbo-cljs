(ns hh-bbo-cljs.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [cljs-time.format :refer [formatter parse]]
            [hh-bbo-cljs.db :as db]
            [ajax.core :as ajax]))

(rf/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(rf/reg-event-db
  :service-purchase-history/change-current-page
  (fn  [db [_ current-page]]
    (assoc-in db [:service-purchase-history :current-page] current-page)))

(defn toggle-sort-direction [sort-direction]
  (case sort-direction
    :desc :asc
    :asc :desc
    :desc))

(rf/reg-event-db
  :service-purchase-history/change-sort-column
  (fn [db [_ sort-column]]
    (let [current-sort-column (-> db :service-purchase-history :sort-column)
          current-sort-direction (-> db :service-purchase-history :sort-direction)]
      (if (= current-sort-column sort-column)
        (-> db (assoc-in [:service-purchase-history :sort-direction] (toggle-sort-direction current-sort-direction)))
        (-> db
            (assoc-in [:service-purchase-history :sort-column] sort-column)
            (assoc-in [:service-purchase-history :sort-direction] :desc))))))

(rf/reg-event-db
  :service-purchase-history/set-search-query
  (fn [db [_ search-query]]
    (-> db
        (assoc-in [:service-purchase-history :search-query] search-query)
        (assoc-in [:service-purchase-history :current-page] 0))))

(def date-time-format (formatter "dd.MM.YYYY HH:mm"))

(rf/reg-event-db
  :service-purchase-history/process-response
  (fn [db [_ response]]
    (let [response (js->clj response)
          response (map (fn [d] (-> d
                                    (update :creation-time #(parse date-time-format %))
                                    (update :activation-time #(parse date-time-format %)))) response)]
      (-> db
          (assoc-in [:service-purchase-history :loading?] false)
          (assoc-in [:service-purchase-history :data] response)))))

(rf/reg-event-db
  :service-purchase-history/bad-response
  (fn [db [_ response]]
    (-> db
        (assoc-in [:service-purchase-history :loading?] false)
        (assoc-in [:service-purchase-history :loading-failed?] true))))

(rf/reg-event-fx
  :service-purchase-history/load-data
  (fn [{db :db} _]
    {:http-xhrio {:method :put
                  :uri "http://www.mocky.io/v2/58d8cfa40f0000711edcc789"
                  :format (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [:service-purchase-history/process-response]
                  :on-failure [:service-purchase-history/bad-response]}
     :db (assoc-in db [:service-purchase-history :loading?] true)}))
