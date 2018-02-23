val records = spark.
  readStream.
  format("kafka").
  option("subscribe", "smartPlug2").
  option("kafka.bootstrap.servers", "mykafkabroker:6667").load
 
records.printSchema
 
val result = records.
  select(
    $"key" cast "string",   
    $"value" cast "string",
    $"topic",
    $"partition",
    $"offset")
 
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import scala.concurrent.duration._
val sq = result.
  writeStream.
  format("console").
  option("truncate", false).
  trigger(Trigger.ProcessingTime(10.seconds)).
  outputMode(OutputMode.Append).
  queryName("scalastrstrclient").
  start
 
sq.status
