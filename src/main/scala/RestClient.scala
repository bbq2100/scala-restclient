import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{BasicResponseHandler, DefaultHttpClient}
import org.apache.http.message.BasicHeader

object RestClient extends App {
  require(args.size >= 2, "At minimum you should specify the HTTP operation (Get, Post, Put, Delete, Options) and the server url")

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
    case "put" => handlePutRequest
    case "delete" => handleDeleteRequest
    case "options" => handleOptionRequest
  }

  def handleGetRequest = {
    def headers = for (header <- params("-h")) yield {
      def tokens = splitByEqual(header)
      new BasicHeader(tokens(0), tokens(1))
    }

    val query = params("-d").mkString("&")
    /**
     * Applying string interpolation (injecting local variables in the result String object) with of StringContext.
     * @see StringContext
     */
    val httpGet = new HttpGet(s"${url}?${query}")
    headers.foreach(httpGet.addHeader(_))
    val responseBody = new DefaultHttpClient().execute(httpGet, new BasicResponseHandler)
    println(responseBody)
  }

  def splitByEqual(header: String) = header.split('=')

  def handleOptionRequest = ???

  def handlePostRequest = ???

  def handlePutRequest = ???

  def handleDeleteRequest = ???

}