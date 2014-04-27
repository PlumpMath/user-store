(ns user-store.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET defroutes]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]))

(defroutes app-routes
  (GET  "/" [] (response {:api_root "true"}))
  (GET  "/users" [] (response [{:name "User 1"} {:name "User 2"}]))
  (route/resources "/")
  (route/not-found (response {:error {:status 404 :body "Not found"}})))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
