package part2dataframes

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DateType, DoubleType, IntegerType, LongType, StringType, StructField, StructType}

object DataFrameBasics extends App {

  // creating a spark session
  val spark = SparkSession.builder()
    .appName("DataFrames Basics")
    .config("spark.master", "local")
    .getOrCreate()

  // reading a DF
  val firstDF = spark.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/cars.json")

  // showing a DF
  //  firstDF.show()
  //  firstDF.printSchema()

  // get rows
  firstDF take 10 foreach println

  // spark types
  val longType = LongType

  // schema
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

  // obtain a schema
  val carsDFSchema = firstDF.schema
  //  println(carsDFSchema)

  // read a DF with your schema
  val carsDFWithSchema = spark.read
    .format("json")
    .schema(carsSchema)
    .load("src/main/resources/data/cars.json")

  // create rows by hand
  val myRow = Row("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA")

  // create DF from tuples
  val cars = Seq(
    ("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA"),
    ("buick skylark 320", 15.0, 8L, 350.0, 165L, 3693L, 11.5, "1970-01-01", "USA"),
    ("plymouth satellite", 18.0, 8L, 318.0, 150L, 3436L, 11.0, "1970-01-01", "USA"),
    ("amc rebel sst", 16.0, 8L, 304.0, 150L, 3433L, 12.0, "1970-01-01", "USA"),
    ("ford torino", 17.0, 8L, 302.0, 140L, 3449L, 10.5, "1970-01-01", "USA"),
    ("ford galaxie 500", 15.0, 8L, 429.0, 198L, 4341L, 10.0, "1970-01-01", "USA"),
    ("chevrolet impala", 14.0, 8L, 454.0, 220L, 4354L, 9.0, "1970-01-01", "USA"),
    ("plymouth fury iii", 14.0, 8L, 440.0, 215L, 4312L, 8.5, "1970-01-01", "USA"),
    ("pontiac catalina", 14.0, 8L, 455.0, 225L, 4425L, 10.0, "1970-01-01", "USA"),
    ("amc ambassador dpl", 15.0, 8L, 390.0, 190L, 3850L, 8.5, "1970-01-01", "USA")
  )

  val manualCarsDF = spark.createDataFrame(cars) // schema auto-inferred

  // note: DFs have schemas, rows do not

  // create DFs with implicits

  import spark.implicits._

  val manualCarsDFWithImplicits = cars.toDF("Name", "MPG", "Cylinders", "Displacement", "HP", "Weight", "Acceleration", "Year", "CountryOrigin")

  //  manualCarsDF.printSchema()
  //  manualCarsDFWithImplicits.printSchema()

  /**
   * Exercise:
   * 1) Create a manual DF describing smart phones
   *    - make
   *    - model
   *    - screen dimension
   *    - camera mega pixels
   *
   * 2) Read another file from the data / folder, e.g. movies.json
   *    - print its schema
   *    - count the number of rows, call count()
   */

  val phones = Seq(
    ("Nokia", "3120", "80x60", "80mp"),
    ("Google", "Pixel", "80x65", "100mp"),
    ("Apple", "iPhone13", "90x60", "110mp"),
    ("Huawei", "Mate", "75x65", "78mp")
  )

  val phonesDF = spark.createDataFrame(phones) // loose column names
  //  phones.toDF("Make", "Model", "Dimensions", "Camera") // have column names

  phonesDF.show()
  phonesDF.printSchema()

  val moviesSchema = StructType(Array(
    StructField("Title", StringType),
    StructField("US_Gross", IntegerType),
    StructField("Worldwide_Gross", IntegerType),
    StructField("US_DVD_Sales", StringType),
    StructField("Production_Budget", IntegerType),
    StructField("Release_Date", DateType),
    StructField("MPAA_Rating", StringType),
    StructField("Running_Time_min", IntegerType),
    StructField("Distributor", StringType),
    StructField("Source", StringType),
    StructField("Major_Genre", StringType),
    StructField("Creative_Type", StringType),
    StructField("Director", StringType),
    StructField("Rotten_Tomatoes_Rating", DoubleType),
    StructField("IMDB_Rating", DoubleType),
    StructField("IMDB_Votes", IntegerType)
  ))

  val moviesDF = spark.read
    .format("json")
    .schema(moviesSchema)
    .load("src/main/resources/data/movies.json")

  moviesDF.show()
  moviesDF.printSchema()
  println(moviesDF.count()) // number of records

}
