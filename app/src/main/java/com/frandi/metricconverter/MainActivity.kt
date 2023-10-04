package com.frandi.metricconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    lateinit var metrikAutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan1AutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan2AutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan1TextInputLayout: TextInputLayout
    lateinit var satuan2TextInputLayout: TextInputLayout
    lateinit var nilaiAwalEditText: EditText
    lateinit var hasilTextView:TextView
    lateinit var rootLayout: LinearLayout

    lateinit var daftarMetrik:Array<String>

    var satuan1: String = ""
    var satuan2: String = ""

    fun initComponents(){
        metrikAutoCompleteTextView = findViewById(R.id.metrikAutoCompleteTextView)
        satuan1AutoCompleteTextView = findViewById(R.id.satuan1AutoCompleteTextView)
        satuan2AutoCompleteTextView = findViewById(R.id.satuan2AutoCompleteTextView)
        nilaiAwalEditText = findViewById(R.id.nilaiAwalEditText)
        hasilTextView = findViewById(R.id.hasilTextView)
        satuan1TextInputLayout = findViewById(R.id.satuan1TextInputLayout)
        satuan2TextInputLayout = findViewById(R.id.satuan2TextInputLayout)
        rootLayout = findViewById(R.id.rootLayout)
    }

    fun initListener(){
        rootLayout.setOnClickListener {
            currentFocus?.clearFocus()
        }

        metrikAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            if(selectedItem == daftarMetrik[0]) setSatuanAdapter(R.array.select_panjang_item)
            if(selectedItem == daftarMetrik[1]) setSatuanAdapter(R.array.select_massa_item)
            if(selectedItem == daftarMetrik[2]) setSatuanAdapter(R.array.select_waktu_item)
            if(selectedItem == daftarMetrik[3]) setSatuanAdapter(R.array.select_arus_listrik_item)
            if(selectedItem == daftarMetrik[4]) setSatuanAdapter(R.array.select_suhu_item)
            if(selectedItem == daftarMetrik[5]) setSatuanAdapter(R.array.select_intensitas_cahaya_item)
            if(selectedItem == daftarMetrik[6]) setSatuanAdapter(R.array.select_jumlah_zat_item)

            metrikAutoCompleteTextView.clearFocus()
            resetAllInput()
            toggleSatuanSelect(true)
        }

        satuan1AutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            satuan1AutoCompleteTextView.clearFocus()
            val selectedItem = parent.getItemAtPosition(position).toString()
            satuan1 = selectedItem

            if(satuan2AutoCompleteTextView.text.toString().isNotEmpty() && nilaiAwalEditText.text.toString().isNotEmpty()){
                convertMetrik(nilaiAwalEditText.text.toString().toDouble())
            }

        }

        satuan2AutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            satuan2AutoCompleteTextView.clearFocus()
            val selectedItem = parent.getItemAtPosition(position).toString()
            satuan2 = selectedItem

            if(satuan1AutoCompleteTextView.text.toString().isNotEmpty() && nilaiAwalEditText.text.toString().isNotEmpty()){
                convertMetrik(nilaiAwalEditText.text.toString().toDouble())
            }
        }

        nilaiAwalEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isEmpty() || s.toString() == "0") {
                    hasilTextView.visibility = View.GONE
                    return
                }
                if(satuan1.isEmpty() || satuan2.isEmpty()) return
                convertMetrik(s.toString().toDouble())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun resetAllInput(){
        satuan1AutoCompleteTextView.setText("")
        satuan2AutoCompleteTextView.setText("")
        nilaiAwalEditText.setText("")
        hasilTextView.setText("")
        hasilTextView.visibility = View.GONE
    }

    fun convertMetrik(nilaiAwal:Double){
        if(satuan1 == satuan2) return setHasil(nilaiAwal,"")

        if(satuan1 == "Meter" && satuan2 == "Kilometer") return setHasil(nilaiAwal/1000, "km")
        if(satuan1 == "Kilometer" && satuan2 == "Meter") return setHasil(nilaiAwal*1000, "m")

        if(satuan1 == "Gram" && satuan2 == "Kilogram") return setHasil(nilaiAwal/1000, "kg")
        if(satuan1 == "Kilogram" && satuan2 == "Gram") return setHasil(nilaiAwal*1000, "g")

        if(satuan1 == "Sekon" && satuan2 == "Menit") return setHasil(nilaiAwal/60, "min")
        if(satuan1 == "Menit" && satuan2 == "Sekon") return setHasil(nilaiAwal*60, "s")

        if(satuan1 == "Ampere" && satuan2 == "Nanoampere") return setHasil(nilaiAwal*1000000000, "nA")
        if(satuan1 == "Nanoampere" && satuan2 == "Ampere") return setHasil(nilaiAwal/1000000000, "A")

        if(satuan1 == "Celcius" && satuan2 == "Fahrenheit") return setHasil((nilaiAwal*9/5)+32, "F")
        if(satuan1 == "Fahrenheit" && satuan2 == "Celcius") return setHasil((nilaiAwal-32)*5/9, "C")

        if(satuan1 == "Candela" && satuan2 == "Lumen") return setHasil(nilaiAwal*12.56637, "lm")
        if(satuan1 == "Lumen" && satuan2 == "Candela") return setHasil(nilaiAwal/12.56637, "cd")

        if(satuan1 == "Mole" && satuan2 == "Kilomole") return setHasil(nilaiAwal/1000, "kmol")
        if(satuan1 == "Kilomole" && satuan2 == "Mole") return setHasil(nilaiAwal*1000, "mol")
    }

    fun setHasil(res:Double,prefix:String){
        hasilTextView.text = res.toString().trimEnd('0').trimEnd('.') + " " + prefix
        hasilTextView.visibility = View.VISIBLE
    }

    fun setSatuanAdapter(arrStringRes:Int){
        satuan1AutoCompleteTextView.setAdapter(ArrayAdapter(this,R.layout.dropdown_select_item,resources.getStringArray(arrStringRes)))
        satuan2AutoCompleteTextView.setAdapter(ArrayAdapter(this,R.layout.dropdown_select_item,resources.getStringArray(arrStringRes)))
    }

    fun initMetrikDropdownAdapterAndListener(){
        daftarMetrik = resources.getStringArray(R.array.daftar_metrik)
        val adapter = ArrayAdapter(this,R.layout.dropdown_select_item,daftarMetrik)
        metrikAutoCompleteTextView.setAdapter(adapter)
    }

    fun toggleSatuanSelect(state:Boolean){
        if(!state){
            satuan1TextInputLayout.isEnabled = false
            satuan2TextInputLayout.isEnabled = false
            nilaiAwalEditText.isEnabled = false
            return
        }
        satuan1TextInputLayout.isEnabled = true
        satuan2TextInputLayout.isEnabled = true
        nilaiAwalEditText.isEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initListener()

        supportActionBar?.setSubtitle("Oleh: Frandi Andika")
        initMetrikDropdownAdapterAndListener()
        toggleSatuanSelect(false)
    }
}