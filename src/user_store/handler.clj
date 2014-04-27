(ns user-store.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET POST defroutes]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [taoensso.carmine :as car :refer (wcar)]))

;; Redis connection
(def redis-server-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar redis-server-conn ~@body))

;; Routes
(defroutes app-routes
  (GET  "/" [] (response {:api_root "true"}))
  (GET  "/users" [] (response [{:name "User 1"} {:name "User 2"}]))
  (POST "/users" [name] (response {:ok {:status 201 :body (str "Created " name)}}))
  (route/resources "/")
  (route/not-found (response {:error {:status 404 :body "Not found"}})))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
