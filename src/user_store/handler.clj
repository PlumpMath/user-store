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

;; TODO: extract
(defn create-user
  [name email password]
  (response {:ok {:status 201 :body (str "Successfully created " name)}}))

;; Routes
(defroutes app-routes
  (GET  "/" [] (response {:ok {:status 200 :body "user-store API"}}))
  (GET  "/users" [] (response {:ok {:status 200 :users [{:name "User 1"} {:name "User 2"}]}}))
  (GET  "/users/:id" [id] (response {:ok {:status 200 :user {:name (str "User " id)}}}))
  (POST "/users" [name email password] (create-user name email password))

  (route/resources "/")
  (route/not-found (response {:error {:status 404 :body "Not found"}})))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
