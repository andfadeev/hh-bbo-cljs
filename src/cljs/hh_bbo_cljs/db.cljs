(ns hh-bbo-cljs.db
  (:require [cljs-time.format :refer [formatter parse]]))

(def date-time-format (formatter "dd.MM.YYYY HH:mm"))

(defn prepare-data []
  (let [data [{:creation-time "28.03.2009 03:22" :activation-time "28.03.2009 03:22" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.04.2009 01:10" :activation-time "28.04.2009 01:10" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}
              {:creation-time "28.08.2009 06:38" :activation-time "28.08.2009 06:38" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.10.2009 00:31" :activation-time "28.10.2009 00:31" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}
              {:creation-time "28.11.2009 00:57" :activation-time "28.11.2009 00:57" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.12.2009 00:33" :activation-time "28.12.2009 00:33" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.01.2010 00:59" :activation-time "28.01.2010 00:59" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 2" :services ["FPN=2"]}
              {:creation-time "28.02.2010 00:59" :activation-time "28.02.2010 00:59" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}
              {:creation-time "28.03.2010 00:32" :activation-time "28.03.2010 00:32" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 2" :services ["FPN=2"]}
              {:creation-time "28.04.2010 00:14" :activation-time "28.04.2010 00:14" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.05.2010 00:13" :activation-time "28.05.2010 00:13" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.06.2010 00:10" :activation-time "28.06.2010 00:10" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 3" :services ["FPN=3"]}
              {:creation-time "28.07.2010 00:13" :activation-time "28.07.2010 00:13" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 2" :services ["FPN=2"]}
              {:creation-time "28.08.2010 00:11" :activation-time "28.08.2010 00:11" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}
              {:creation-time "28.09.2010 00:10" :activation-time "28.09.2010 00:10" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}
              {:creation-time "28.10.2010 00:12" :activation-time "28.10.2010 00:12" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 2" :services ["FPN=2"]}
              {:creation-time "28.11.2010 00:10" :activation-time "28.11.2010 00:10" :expiration-time "" :price 0 :manager "Headhunterru System User hhsystem" :title "Бесплатные вакансии: 1" :services ["FPN=1"]}]]
    (map (fn [d] (-> d
                     (update :creation-time #(parse date-time-format %))
                     (update :activation-time #(parse date-time-format %)))) data)))

(def default-db
  {:service-purchase-history {:data {}
                              :search-query nil
                              :sort-column :creation-time
                              :sort-direction :desc
                              :current-page 0
                              :page-size 5}})
