package com.cflewis.jms

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

case class Producer(factory:ConnectionFactory, queueName:String) {
    val connection = factory.createConnection()
    connection.start()
    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val destination = session.createQueue(queueName)
    val producer = session.createProducer(destination)
    
    def run() = {
        for (i <- 0 until 100) {
            println("Sending message")
            var message = session.createTextMessage("Number " + i + ": Hello world!")
            producer.send(message)
        }
    }
    
    def close() = {
        if (connection != null) { connection.close() }
    }
}


object ProducerApp extends Application{
  val brokerUrl = "tcp://localhost:61616"
  
  val factory:ConnectionFactory = new ActiveMQConnectionFactory(brokerUrl)
  val producer:Producer =Producer(factory, "test")
  producer.run()
  producer.close()
}
