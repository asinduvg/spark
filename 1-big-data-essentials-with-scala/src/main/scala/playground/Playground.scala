package playground

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

/**
 * This is a small application that loads some manually inserted rows into a Spark DataFrame.
 * Feel free to modify this code as you see fit, fiddle with the code and play with your own exercises, ideas and datasets.
 *
 * Daniel @ Rock the JVM
 */
object Playground extends App {

  /**
   * This creates a SparkSession, which will be used to operate on the DataFrames that we create.
   */
  val spark = SparkSession.builder()
    .appName("Spark Essentials Playground App")
    .config("spark.master", "local")
    .getOrCreate()

  /**
   * The SparkContext (usually denoted `sc` in code) is the entry point for low-level Spark APIs, including access to Resilient Distributed Datasets (RDDs).
   */
  val sc = spark.sparkContext

  /**
   * A Spark schema structure that describes a small cars DataFrame.
   */
  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", StringType),
    StructField("Origin", StringType)
  ))

  /**
   * A "manual" sequence of rows describing cars, fetched from cars.json in the data folder.
   */


  /**
   * The two lines below create an RDD of rows (think of an RDD like a parallel collection).
   * Then from the RDD we create a DataFrame, which has a number of useful querying methods.
   */
//  val carsRows = sc.parallelize(cars)
//  val carsDF = spark.createDataFrame(carsRows, carsSchema)

  /**
   * If the schema and the contents of the DataFrame are printed correctly to the console,
   * this means the libraries work correctly and you can jump into the course!
   */
//  carsDF.printSchema()
//  carsDF.show()

}
