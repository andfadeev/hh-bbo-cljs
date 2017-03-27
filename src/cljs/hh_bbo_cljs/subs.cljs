(ns hh-bbo-cljs.subs
    (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as rf]
            [cljs-time.core :refer [before? after?]]
            [cljs-time.format :refer [formatter unparse]]
            [re-frame.subs :as subs]
            [clojure.string :as str]))

(rf/reg-sub
  :service-purchase-history/data
  (fn [db]
    (-> db :service-purchase-history :data)))

(rf/reg-sub
  :service-purchase-history/items-count
  (fn [_ _]
    (subs/subscribe [:service-purchase-history/sorted-data]))
  (fn [items] (count items)))

(rf/reg-sub
  :service-purchase-history/available-pages
  (fn [_ _]
    [(subs/subscribe [:service-purchase-history/items-count])
     (subs/subscribe [:service-purchase-history/page-size])])
  (fn [[items-count page-size] _]
    (range 1 (inc (js/Math.round (/ items-count page-size))))))

(defn datetime-sorter [sort-column sort-direction items]
  (sort-by sort-column (if (= sort-direction :desc) after? before?) items))

(defn simple-sorter [sort-column sort-direction items]
  (let [sorted (sort-by sort-column items)]
    (if (= sort-direction :desc) sorted (reverse sorted))))

(defmulti service-purchase-history-sorter (fn [sort-column _ _] sort-column))

(defmethod service-purchase-history-sorter
  :creation-time
  [sort-column sort-direction items]
  (datetime-sorter sort-column sort-direction items))

(defmethod service-purchase-history-sorter
  :activation-time
  [sort-column sort-direction items]
  (datetime-sorter sort-column sort-direction items))

(defmethod service-purchase-history-sorter
  :default
  [sort-column sort-direction items]
  (simple-sorter sort-column sort-direction items))

(def date-time-format (formatter "dd.MM.YYYY HH:mm"))

(defmulti as-string (fn [[col val]] col))

(defmethod as-string
  :activation-time
  [[_ val]]
  (unparse date-time-format val))

(defmethod as-string
  :creation-time
  [[_ val]]
  (unparse date-time-format val))

(defmethod as-string
  :default
  [[_ val]]
  (str val))

(defn service-purchase-history-filter [[sort-column sort-direction search-query items current-page page-size] & _]
  (let [sorted-items (service-purchase-history-sorter sort-column sort-direction items)]
    (if (str/blank? search-query)
      sorted-items
      (filter (fn [item] (some (fn [kv] (str/includes? (as-string kv) search-query)) item)) sorted-items))))

(rf/reg-sub
  :service-purchase-history/sorted-data
  (fn [_ _]
    [(subs/subscribe [:service-purchase-history/sort-column])
     (subs/subscribe [:service-purchase-history/sort-direction])
     (subs/subscribe [:service-purchase-history/search-query])
     (subs/subscribe [:service-purchase-history/data])])
  service-purchase-history-filter)

(rf/reg-sub
  :service-purchase-history/page
  (fn [_ _]
    [(subs/subscribe [:service-purchase-history/sorted-data])
     (subs/subscribe [:service-purchase-history/current-page])
     (subs/subscribe [:service-purchase-history/page-size])])
  (fn [[sorted-items current-page page-size]]
    (take page-size (drop (* current-page page-size) sorted-items))))

(rf/reg-sub
  :service-purchase-history/search-query
  (fn [db]
    (-> db :service-purchase-history :search-query)))

(rf/reg-sub
  :service-purchase-history/sort-column
  (fn [db]
    (-> db :service-purchase-history :sort-column)))

(rf/reg-sub
  :service-purchase-history/sort-direction
  (fn [db]
    (-> db :service-purchase-history :sort-direction)))

(rf/reg-sub
  :service-purchase-history/page-size
  (fn [db]
    (-> db :service-purchase-history :page-size)))

(rf/reg-sub
  :service-purchase-history/current-page
  (fn [db]
    (-> db :service-purchase-history :current-page)))

(rf/reg-sub
  :service-purchase-history/loading?
  (fn [db]
    (-> db :service-purchase-history :loading?)))

(rf/reg-sub
  :service-purchase-history/loading-failed?
  (fn [db]
    (-> db :service-purchase-history :loading-failed?)))


