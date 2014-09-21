import java.util

import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpOptions, HttpDelete, HttpGet, HttpPost}
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient}
import org.apache.http.message.{BasicHeader, BasicNameValuePair}

/**
 * Provides a convenient way to call RESTful webservices.
 * How to run: sbt run (post | get | delete | options) -d <request parameters comma separated -h <headers comma separated> <url>
 * (at least you should specify action(post, get, delete, options) and server url)
 */
object RestClient extends App {
  require(args.size >= 2, "At minimum you should specify the HTTP operation (Get, Post, Delete, Options) and the server url")

  val command = args.head
  val params = parseArgs(args)
  val url = args.last

  def parseArgs(args: Array[String]): Map[String, List[String]] = {

    def nameValuePair(paramName: String) = {

      def values(commaSeparatedValues: String) = {
        commaSeparatedValues.split(",").toList
      }

      val index = args.indexWhere(_ == paramName)
      (paramName, if (index == -1) Nil else values(args(index + 1)))
    }

    Map(nameValuePair("-d"), nameValuePair("-h"))
  }

  command match {
    case "get" => handleGetRequest
    case "post" => handlePostRequest
    case "delete" => handleDeleteRequest
    case "options" => handleOptionRequest
  }

  def headers = for (header <- params(key = "-h")) yield {
    def tokens = splitByEqual(header)
    new BasicHeader(tokens(0), tokens(1))
  }

  def handleGetRequest = {
    val query = params("-d").mkString("&")
    /**
     * Applying string interpolation (injecting local variables in the result String object) via StringContext.
     * @see StringContext
     */
    val httpGet = new HttpGet(s"${url}?${query}")
    headers.foreach(httpGet.addHeader(_))
    val responseBody = new DefaultHttpClient().execute(httpGet, new BasicResponseHandler)
    println(responseBody)
  }

  def splitByEqual(header: String) = header.split('=')

  def formEntity = {
    def toJavaList(scalaList: List[BasicNameValuePair]) = {
      /**
       * Enforcing to send the result of toArray() as variable argument to the asList().
       * Otherwise asList() will create only one item.
       * @see List.toArray:_*
       */
      util.Arrays.asList(scalaList.toArray:_*)
    }

    def formParams = for (nameValue <- params("-d")) yield {
      def tokens = splitByEqual(nameValue)
      new BasicNameValuePair(tokens(0), tokens(1))
    }

    def formEntity = new UrlEncodedFormEntity(toJavaList(formParams), "UTF-8")
    formEntity
  }

  def handlePostRequest = {
    val httpPost: HttpPost = new HttpPost(url)
    headers.foreach(httpPost.addHeader(_))
    httpPost.setEntity(formEntity)
    val responseBody = new DefaultHttpClient().execute(httpPost, new BasicResponseHandler)
    println(responseBody)
  }

  def handleDeleteRequest = {
    val httpDelete = new HttpDelete(url)
    val httpResponse = new DefaultHttpClient().execute(HttpDelete)
    println(httpResponse.getStatusLine)
  }

  def handleOptionRequest = {
    val httpOptions = new HttpOptions(url)
    headers.foreach(httpOptions.addHeader(_))
    val httpResponse = new DefaultHttpClient().execute(httpOptions)
    println(httpOptions.getAllowedMethods(httpResponse))
  }

}