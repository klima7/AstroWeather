package com.klima7.astroweather.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.android.volley.Response;
import com.klima7.astroweather.Unit;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class YahooWeatherRequest extends YahooRequest<Weather> {

    public static final String IMAGE_URL_FORMAT = "http://l.yimg.com/a/i/us/we/52/%d.gif";

    private int woeid;
    private Unit unit;

    public YahooWeatherRequest(int woeid, Unit unit, Response.Listener<Weather> listener, Response.ErrorListener errorListener) {
        super(String.format("?woeid=%d&format=xml&u=%s", woeid, unit.getCode()), listener, errorListener);
        setShouldCache(false);
        this.woeid = woeid;
        this.unit = unit;
    }

    @Override
    public Weather parseResponse(String response) {
        Document document = convertStringToXMLDocument(response);
        Weather weather = new Weather();

        NamedNodeMap windAttributes = document.getElementsByTagName("yweather:wind").item(0).getAttributes();
        weather.chill = Integer.parseInt(windAttributes.getNamedItem("chill").getTextContent());
        weather.speed = Double.parseDouble(windAttributes.getNamedItem("speed").getTextContent());
        weather.direction = Integer.parseInt(windAttributes.getNamedItem("direction").getTextContent());

        NamedNodeMap atmosphereAttributes = document.getElementsByTagName("yweather:atmosphere").item(0).getAttributes();
        weather.humidity = Integer.parseInt(atmosphereAttributes.getNamedItem("humidity").getTextContent());
        weather.pressure = Double.parseDouble(atmosphereAttributes.getNamedItem("pressure").getTextContent());
        weather.visibility = Double.parseDouble(atmosphereAttributes.getNamedItem("visibility").getTextContent());

        NamedNodeMap conditionAttributes = document.getElementsByTagName("yweather:condition").item(0).getAttributes();
        weather.code = Integer.parseInt(conditionAttributes.getNamedItem("code").getTextContent());
        weather.date = conditionAttributes.getNamedItem("date").getTextContent();
        weather.temperature = Integer.parseInt(conditionAttributes.getNamedItem("temp").getTextContent());
        weather.text = conditionAttributes.getNamedItem("text").getTextContent();

        List<Forecast> forecastsList = new ArrayList<>();
        NodeList forecasts = document.getElementsByTagName("yweather:forecast");
        for(int i=0; i<forecasts.getLength(); i++) {
            Node forecastNode = forecasts.item(i);
            Forecast forecast = parseForecastNode(forecastNode);
            forecastsList.add(forecast);
        }
        weather.forecasts = forecastsList;

        weather.image = getImage(weather.code);

        weather.woeid = woeid;
        weather.unit = unit;

        return weather;
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Forecast parseForecastNode(Node node) {
        Forecast forecast = new Forecast();

        NamedNodeMap attributes = node.getAttributes();
        forecast.low = Integer.parseInt(attributes.getNamedItem("low").getTextContent());
        forecast.high = Integer.parseInt(attributes.getNamedItem("high").getTextContent());
        forecast.code = Integer.parseInt(attributes.getNamedItem("code").getTextContent());
        forecast.day = attributes.getNamedItem("day").getTextContent();
        forecast.date = attributes.getNamedItem("date").getTextContent();
        forecast.text = attributes.getNamedItem("text").getTextContent();
        forecast.image = getImage(forecast.code);

        return forecast;
    }

    private String getImage(int code) {
        URL url;
        try {
            url = new URL(String.format(IMAGE_URL_FORMAT, code));
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return encodeTobase64(bitmap);

        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
}
