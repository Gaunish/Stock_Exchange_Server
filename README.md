# Stock Exchange Server(Java)

* **By Zian Wang(zw142) and Gaunish Garg(gg147)**

* **Note: writeup/ and testing/ are both under the /src directory**

* The project involves a multi-threaded stock server implemented using Java, gradle. It communicates with clients using XML(JDOM library) clients test scalibility of the server with concurrent requests



=============== Run Server ================

$ sudo docker-compose up

(Listening on port 12345)

================ Run Client =================

$ cd src

$ make clean

$ make -k

$ java testing/Client [server_address] [number_of_threads]




