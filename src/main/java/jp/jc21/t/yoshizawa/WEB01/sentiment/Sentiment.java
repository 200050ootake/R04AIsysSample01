package jp.jc21.t.yoshizawa.WEB01.sentiment;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import jp.jc21.t.yoshizawa.WEB01.WebApiConnector;

public class Sentiment {

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		Language message = getSentiment("Stepover Toehold With Facelock");
		if (message != null) {
			System.out.println("positive:"+message.documents[0].confidenceScores.positive);
			System.out.println("neutral:"+message.documents[0].confidenceScores.neutral);
			System.out.println("negative:"+message.documents[0].confidenceScores.negative);
		}
	}

	static Language getSentiment(String s) throws IOException, URISyntaxException, InterruptedException {
		Gson gson = new Gson();

		String url = "https://r04jk3a04-text.cognitiveservices.azure.com/" + "text/analytics/v3.0/sentiment";
		Map<String, String> map = new HashMap<>();
		map.put("Ocp-Apim-Subscription-Key", "b14f2cddb21342fcb9c10ee1b41b983b");

		Docs doc = new Docs();
		doc.id = "1";
		doc.text = s;

		Source src = new Source();
		src.documents = new Docs[1];
		src.documents[0] = doc;

		String jsonData = new Gson().toJson(src);

		InetSocketAddress proxy = new InetSocketAddress("172.17.0.2", 80);

		JsonReader reader = WebApiConnector.postJsonReader(url, proxy, map, jsonData);
		Language message = null;
		if (reader != null) {
			message = gson.fromJson(reader, Language.class);
			reader.close();
		}
		return message;
	}

}

class Language {
	Documents[] documents;
	String[] errors;
	String modelVersion;
}

class Documents {
	String sentiment;
	ConfidenceScore 	confidenceScores;
}

class 	ConfidenceScore {
	float positive;;
	float neutral;;
	float negative;;
}

class Source {
	Docs[] documents;
}

class Docs {
	String id;
	String text;
}
