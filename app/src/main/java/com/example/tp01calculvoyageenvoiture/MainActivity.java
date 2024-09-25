package com.example.tp01calculvoyageenvoiture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public String gouvernorats[] = {"Bizerte", "Sousse", "Tunis"};
    public String TypeEssence[] = {"Super Sans Plomb","Gasoil Super", "Gasoil"};

    public String source= "Bizerte"; // valeurs par défaut des spinners
    public String destination= "Bizerte";// valeurs par défaut des spinners
    String typeEss = "Super Sans Plomb";
    //Tunis --> sousse: 149.68
    //Tunis --> Bizerte: 71.01
    //Souuse --> Bizerte: 212.10


    public Double distances[][] = {
            {0.0,212.10,71.01},
            {212.10,0.0,149.68},
            {71.01,149.68,0.0}
    };

    //Déclaration des variables
    EditText _txtConsoEssence;
    TextView _txtDistance,_txtResultat,_txtPrixLitreEssence;
    Spinner _spnSource,_spnDestination,_spnTypeEssence;
    RadioButton _rdbRouteNationale,_rdbAutoroute;
    CheckBox _chkEau,_chkCafe,_chkPizza;
    Button _btnCalculerCoutVoyage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //liaison des variables
        _txtConsoEssence = (EditText) findViewById(R.id.txtConsoEssence);
        _txtDistance = (TextView) findViewById(R.id.txtDistance);
        _txtResultat = (TextView) findViewById(R.id.txtResultat);
        _txtPrixLitreEssence = (TextView) findViewById(R.id.txtPrixLitreEssence);
        _spnSource = (Spinner) findViewById(R.id.spnSource);
        _spnDestination = (Spinner) findViewById(R.id.spnDestination);
        _spnTypeEssence = (Spinner) findViewById(R.id.spnTypeEssence);
        _rdbRouteNationale = (RadioButton)findViewById(R.id.rdbRouteNationale);
        _rdbAutoroute = (RadioButton)findViewById(R.id.rdbAutoroute);
        _chkEau = (CheckBox)findViewById(R.id.chkEau);
        _chkCafe = (CheckBox)findViewById(R.id.chkCafe);
        _chkPizza = (CheckBox)findViewById(R.id.chkPizza);
        _btnCalculerCoutVoyage = (Button) findViewById(R.id.btnCalculerCoutVoyage);

        //remplir les spinners (combobox) avec les données coorespondants
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gouvernorats);
        _spnSource.setAdapter(adapt);
        _spnDestination.setAdapter(adapt);

        ArrayAdapter<String> adaptTEssence = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,TypeEssence);
        _spnTypeEssence.setAdapter(adaptTEssence);

        //calculer distances
        _spnSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double dis = 0.0;
                source = _spnSource.getSelectedItem().toString();
                dis = selectDistance(source,destination);
                _txtDistance.setText(dis.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _spnDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double dis = 0.0;
                destination = _spnDestination.getSelectedItem().toString();
                dis = selectDistance(source,destination);
                _txtDistance.setText(dis.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //afficher prix essence
        _spnTypeEssence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double prixess = 0.0;
                typeEss = _spnTypeEssence.getSelectedItem().toString();
                prixess = PrixEssence(typeEss);
                _txtPrixLitreEssence.setText(prixess.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //calculer cout voyage
        _btnCalculerCoutVoyage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v1 = _txtConsoEssence.getText().toString().trim();
                Double consVoiture = 0.0;
                if (v1.isEmpty()){
                    consVoiture = 0.0;
                } else {
                    consVoiture = Double.parseDouble(_txtConsoEssence.getText().toString().trim());

                }
                Double distanceKm = Double.parseDouble(_txtDistance.getText().toString().trim());
                Double prixEssence = Double.parseDouble(_txtPrixLitreEssence.getText().toString().trim());
                Double approv = 0.0;
                //par défaut choisir route nationale
                Double peage = 0.0;
                if (distanceKm > 0.0){
                    if (_chkEau.isChecked()){
                        approv = approv + 1.5;
                    }
                    if (_chkCafe.isChecked()){
                        approv = approv + 2.5;
                    }
                    if (_chkPizza.isChecked()){
                        approv = approv + 6.0;
                    }
                    if (_rdbAutoroute.isChecked()){
                        if ((source.equals("Bizerte") && destination.equals("Tunis")) || (source.equals("Tunis") && destination.equals("Bizerte")) )
                            approv = approv + 1.400;
                        else  if ((source.equals("Sousse") && destination.equals("Tunis")) || (source.equals("Tunis") && destination.equals("Sousse")) )
                            approv = approv + 3.200;
                        else  if ((source.equals("Bizerte") && destination.equals("Sousse")) || (source.equals("Sousse") && destination.equals("Bizerte")) )
                            approv = approv + 4.600;
                    }
                }
                Double montantTotalEssence = (Double) ((consVoiture/100)*distanceKm * prixEssence );
                Double montantTotal =montantTotalEssence + approv;
                DecimalFormat f = new DecimalFormat("##.000");
                String formattedValue = f.format(montantTotal);
                _txtResultat.setText("Le Total est " + formattedValue + " DT");

            }
        });



    }

    private int returnIndex(String str) {
        int index = -1;
        for (int i = 0 ; i< gouvernorats.length;i++){
            if (gouvernorats[i].equalsIgnoreCase(str)){
                index = i;
                break;
            }
        }
        return index;
    }

    public double selectDistance (String source, String destination){
        int iSource, iDestination;
        iSource = returnIndex(source);
        iDestination = returnIndex(destination);
        return distances[iSource][iDestination];
    }

    public  Double PrixEssence(String typeEssence){
        Double prix = 0.0;
        switch (typeEssence ){
            case "Super Sans Plomb":
                prix = 1.945;
                break;
            case "Gasoil Super":
                prix = 1.725;
                break;
            case "Gasoil":
                prix = 1.490;
                break;
            default:
                prix = 0.0;
                break;
        }
        return prix;
    }








}