package lk.nibm.holidayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.json.JSONObject

class Holidaydetails : AppCompatActivity() {

    lateinit var holidayname : TextView
    lateinit var holidaydescription : TextView
    lateinit var holidaycountry : TextView
    lateinit var holidaydate : TextView
    lateinit var holidaytype : TextView
    lateinit var holidaylocation : TextView
    lateinit var holidaystates : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaydetails)

        holidayname = findViewById(R.id.txt_holidayname)
        holidaydescription = findViewById(R.id.txt_description)
        holidaycountry = findViewById(R.id.txt_country)
        holidaydate = findViewById(R.id.txt_date)
        holidaytype = findViewById(R.id.txt_type)
        holidaylocation = findViewById(R.id.txt_location)
        holidaystates = findViewById(R.id.txt_states)

       val holidayString = intent.getStringExtra("holiday")
        val jsonObject = JSONObject(holidayString)

        var name  = jsonObject.getString("name")
        var description = jsonObject.getString("description")
        var country = jsonObject.getJSONObject("country").getString("name")
        var date = jsonObject.getJSONObject("date").getString("iso")
        var type = jsonObject.getJSONArray("type").toString()
        var location = jsonObject.getString("locations")
        var states = jsonObject.getString("states")

        holidayname.text = name
        holidaydescription.text = description
        holidaycountry.text = country
        holidaydate.text = date
        holidaytype.text = type
        holidaylocation.text = location
        holidaystates.text = states









    }
}