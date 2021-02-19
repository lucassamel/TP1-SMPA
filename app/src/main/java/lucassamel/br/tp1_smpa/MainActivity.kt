package lucassamel.br.tp1_smpa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {

    val FINE_REQUEST = 54321

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnObter = btnCoordenadas
        btnObter.setOnClickListener {
            carregarCordenadas()
            criarAquivo()
        }

        btnListarArquivos.setOnClickListener {
            val intent = Intent(this, ListaArquivosActivity::class.java)
            var nomeArquivo = Calendar.getInstance().time;
            intent.putExtra("arquivo","$nomeArquivo.crd")

            startActivity(intent)
        }
    }

    fun criarAquivo(){
        var nomeArquivo = Calendar.getInstance().time;
        val file = File(getExternalFilesDir(null), "$nomeArquivo.crd")

        Log.i("TP1", "Carregou o nome do arquivo $nomeArquivo")

        if(file.exists()){
            file.delete()
            Log.i("TP1", "O arquivo foi deletado")
        }
        else{
            try {
                val os: OutputStream = FileOutputStream(file)

                os.write("Pequeno Teste".toByteArray())
                os.close()

                Toast.makeText(this, "Localização salva com sucesso!", Toast.LENGTH_LONG).show()
                Log.i("TP1", "Arquivo criado")
            } catch (e: IOException) {
                Log.d("Permissao", "Erro de escrita em arquivo")
            }
        }
    }

    private fun carregarCordenadas(){

        Log.i("TP1", "Iniciando Carregador de coordenadas.")
        try {
            val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var location: Location? = null

            if(isGpsEnable){
                if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            2000L,
                            0f,
                            this
                    )

                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val lblValorLatitude = this.findViewById<TextView>(R.id.lblValorLatitude)
                    val lblValorLongitude = this.findViewById<TextView>(R.id.lblValorLongitude)

                    lblValorLatitude.text = location?.latitude.toString()
                    lblValorLongitude.text = location?.latitude.toString()

                } else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
                }
            }


//            if(isNetEnable){
//                Log.i("TP1", "Obtendo localizacao pela rede")
//                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            2000L,
//                            0F,
//                            this
//                    )
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                    lblValorLatitude.text = location?.latitude.toString()
//                    lblValorLongitude.text = location?.latitude.toString()
//                }
//            }else{
//                Log.i("TP1", "Rede não habilitada para localizacao")
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_REQUEST)
//            }


            if(isGpsEnable){
                Log.i("TP1", "Obtendo localizacao pelo GPS")
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            2000L,
                            0F,
                            this
                    )
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    lblLatitude.text = location?.latitude.toString()
                    lblLongitude.text = location?.latitude.toString()
                }
            }else{
                Log.i("TP1", "GPS nao habilitado para a localizacao")
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)
            }



        }catch (ex: SecurityException){

            Log.i("TP1", "Erro na localizacao ")
        }
        Log.i("TP1", "Fim no carregamento das coordenadas")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            carregarCordenadas()
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }



}