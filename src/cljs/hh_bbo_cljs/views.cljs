(ns hh-bbo-cljs.views
  (:require [re-com.core :as re-com]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [cljs-time.format :refer [formatter unparse]]
            [clojure.string :as str]))

;; datetime format in one place (some util ns)
(def date-time-format (formatter "dd.MM.YYYY HH:mm"))

(defn service-purchase-table-row
  "Функция которая рендерит строку в таблице история пополнения"
  [service-purchase-row]
  [:tr
   [:td (unparse date-time-format (:creation-time service-purchase-row))]
   [:td (unparse date-time-format (:activation-time service-purchase-row))]
   [:td (:expiration-time service-purchase-row)]
   [:td (:price service-purchase-row) " руб."]
   [:td (:manager service-purchase-row)]
   [:td (:title service-purchase-row)]
   [:td (for [service (:services service-purchase-row)]
          [:div service])]
   [:td ""]])

(defn sort-icon [column sort-column sort-direction]
  (when (= column sort-column)
    (if (= sort-direction :desc)
      [:i.zmdi.zmdi-caret-down.zmdi-hc-lg]
      [:i.zmdi.zmdi-caret-up.zmdi-hc-lg])))

(defn change-sort-column [column]
  (fn [e]
    (.preventDefault e)
    (rf/dispatch [:service-purchase-history/change-sort-column column])))

(defn service-purchase-thead []
  (let [sort-column @(rf/subscribe [:service-purchase-history/sort-column])
        sort-direction @(rf/subscribe [:service-purchase-history/sort-direction])]
    [:thead
     [:tr
      [:th {:style {:cursor "pointer"}
            :on-click (change-sort-column :creation-time)}
       "Дата создания услуги " [sort-icon :creation-time sort-column sort-direction]]
      [:th {:style {:cursor "pointer"}
            :on-click (change-sort-column :activation-time)}
       "Дата активации услуги " [sort-icon :activation-time sort-column sort-direction]]
      [:th "Дата сгорания услуги"]
      [:th {:style {:cursor "pointer"}
            :on-click (change-sort-column :price)}
       "Стоимость " [sort-icon :price sort-column sort-direction]]
      [:th "Автор"]
      [:th {:style {:cursor "pointer"}
            :on-click (change-sort-column :title)}
       "Название " [sort-icon :title sort-column sort-direction]]
      [:th "Состав"]
      [:th "Код MDS"]]]))

(defn service-purchase-pager []
  (let [available-pages @(rf/subscribe [:service-purchase-history/available-pages])
        current-page @(rf/subscribe [:service-purchase-history/current-page])]
    (when (> (count available-pages) 1)
      [:div.b-admin-billing-pager
       [:div.b-admin-pagination
        (for [page available-pages]
          (if (= (dec page) current-page)
            [:strong page " "]
            [:a {:on-click (fn [e]
                             (.preventDefault e)
                             (rf/dispatch [:service-purchase-history/change-current-page (dec page)]))} page " "]))]])))

(defn reload-link []
  [:a {:on-click (fn [e]
                   (.preventDefault e)
                   (rf/dispatch [:service-purchase-history/load-data]))} "обновить"])

(defn service-purchase-search []
  (let [search-query (rf/subscribe [:service-purchase-history/search-query])]
    (fn []
      [:div
       [:input {:placeholder "Поиск"
                :style {:min-width "250px"}
                :value @search-query
                :on-change #(rf/dispatch [:service-purchase-history/set-search-query (-> % .-target .-value)])
                :on-key-press (fn [e] (when (= 13 (.-charCode e)) (.preventDefault e)))}]])))

(defn service-purchase-block []
  (let [service-purchase-history-page (rf/subscribe [:service-purchase-history/page])
        loading? (rf/subscribe [:service-purchase-history/loading?])
        loading-failed? (rf/subscribe [:service-purchase-history/loading-failed?])]
    (fn []
      [:div
       [:h3.b-subtitle "История расходования"]
       (if @loading?
         [:div "Загрузка данных..."]
         (if @loading-failed?
           [:div "Не удалось загрузить данные, " [reload-link]]
           [:div
            [service-purchase-search]
            [:table.b-admin-table
             [service-purchase-thead]
             [:tbody
              ;;todo move paging logic to subs (take @page-size (drop (* @current-page @page-size) @service-purchase-history))
              (for [service-purchase-item @service-purchase-history-page]
                [service-purchase-table-row service-purchase-item])]]
            [service-purchase-pager]]))])))
