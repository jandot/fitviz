(ns fitviz.core)

(require ['oauth.client :as 'oauth])
;(require ['org.clojure/data.json :as 'json])
(use '[clojure.data.json :only (read-json json-str)])
(require '[clj-http.client :as client])

(def today (java.util.Date.))

(defn today-as-str
  [] (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") today))

(defn get-url
  ([action date] (str "http://api.fitbit.com/1/user/-/" action "/date/" date ".json")))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))
  (def token "2b4ed624ed074e83abe3c8a32533fa79")
  (def secret "iZ4Ra5uxIeLQh2piJFGyBktFK2NYp5f5rsdPr9ynrg")
  (def consumer (oauth/make-consumer token
                                   secret
                                   "http://api.fitbit.com/oauth/request_token"
                                   "http://api.fitbit.com/oauth/access_token"
                                   "http://api.fitbit.com/oauth/authorize"
                                   :hmac-sha1))

  (def request-token (oauth/request-token consumer))

  (def auth-url (oauth/user-approval-uri consumer 
                         (:oauth_token request-token)))

  (println auth-url)
  (println "FitBit PIN:")
  (def pin (read-line))

  (oauth/user-approval-uri consumer 
                              (:oauth_token request-token))

  (def access-token-response (oauth/access-token consumer 
                                    request-token
                                    pin))

  (println access-token-response)

  (def data (client/get (get-url "steps" (today-as-str)) {:oauth-token (access-token-response :oauth_token)}))

  (println data)

  ;(def todays-steps (read-json
  ;                  (:content (client/get (get-url "steps" (today-as-str))
  ;                   :headers {"Authorization" (oauth/authorization-header
  ;                      (oauth/credentials consumer
  ;                        token
  ;                        secret
  ;                        :GET
  ;                        (get-url "steps" (today-as-str))))}
  ;                    :as :string))))

  ;(println todays-steps)
