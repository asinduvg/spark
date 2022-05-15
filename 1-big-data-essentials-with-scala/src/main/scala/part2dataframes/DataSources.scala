package part2dataframes

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types._

object DataSources extends App {

  val spark = SparkSession.builder()
    .appName("Data Sources and Formats")
    .config("spark.master", "local")
    .getOrCreate()

  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", DateType),
    StructField("Origin", StringType)
  ))

  /*
    Reading a DF:
      - format
      - schema or inferSchema = true
      - path
      - zero or more options
   */

  val carsDF = spark.read
    .format("json")
    .schema(carsSchema) // enforce a schema
    .option("mode", "failFast") // dropMalformed, permissive (default), failFast reports malformed records
    .option("path", "src/main/resources/data/cars.json")
    .load()

  //  carsDF.show()

  // alternative reading with options map
  val carsDFWithOptionMap = spark.read
    .format("json")
    .options(Map(
      "mode" -> "failFast",
      "path" -> "src/main/resources/data/cars.json",
      "inferSchema" -> "true"
    ))
    .load()

  /*
    Writing DFs
      - format
      - save mode = overwrite, append, ignore, errorIfExists
      - path
      - zero or more options
   */
  //    carsDF.write
  //      .format("json")
  //      .mode(SaveMode.Overwrite)
  //      .save("src/main/resources/data/cars_dupe.json")

  // JSON flags
  spark.read
    .schema(carsSchema)
    .option("dateFormat", "yyyy-MM-dd") // couple with schema; if spark fails parsing, it will put NULL.
    .option("allowSingleQuotes", "true")
    .option("compression", "uncompressed") // bzip2, gzip, lz4, snappy, deflate
    .json("src/main/resources/data/cars.json")

  // CSV flags
  val stocksSchema = StructType(Array(
    StructField("symbol", StringType),
    StructField("date", DateType),
    StructField("price", DoubleType)
  ))

  spark.read
    .format("csv")
    .schema(stocksSchema)
    .option("dateFormat", "MMM-dd-yyyy")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .load("src/main/resources/data/stocks.csv")

  // Parquet
  //  carsDF.write
  //    .mode(SaveMode.Overwrite)
  //    .parquet("src/main/resources/data/cars.parquet")

  // Text files
  spark.read.text("src/main/resources/data/sampleTextFile.txt").show()

  val driver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://localhost:5432/rtjvm"
  val user = "docker"
  val password = "docker"

  // Reading from a remote DB
  val employeesDF = spark.read
    .format("jdbc")
    .option("driver", driver)
    .option("url", url)
    .option("user", user)
    .option("password", password)
    .option("dbtable", "public.employees")
    .load()

  employeesDF.show()

  /**
   * Exercise: read the movies DF, then write it as
   *  - tab-separated values file
   *  - snappy Parquet
   *  - table "public.movies" in the Postgres DB
   */

  val moviesDF = spark.read
    .json("src/main/resources/data/movies.json")

  moviesDF.write
    .format("csv")
    .option("header", "true")
    .option("sep", "\\t")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/data/movies.csv")

  // Parquet
  moviesDF.write.mode(SaveMode.Overwrite).save("src/main/resources/data/movies.parquet")

  // save to DF
  moviesDF.write
    .format("jdbc")
    .option("driver", driver)
    .option("url", url)
    .option("user", user)
    .option("password", password)
    .option("dbtable", "public.movies")
    .mode(SaveMode.Overwrite)
    .save()

}
