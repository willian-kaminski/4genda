package com.example.nelso.a4genda.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nelso.a4genda.DAO.ContatoDAO;
import com.example.nelso.a4genda.DAO.IContatoDAO;
import com.example.nelso.a4genda.R;
import com.example.nelso.a4genda.adapter.ContatoAdapter;
import com.example.nelso.a4genda.model.Contato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements IContatoDAO {

    private FloatingActionButton fab;
    private List<Contato> contatoList;
    private ContatoDAO dao;
    protected ProgressDialog progressDialog;
    private TextView semContato;
    private RecyclerView rvContatos;
    private RecyclerView.OnItemTouchListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        showLoading();
        fab = findViewById(R.id.fab);
        dao = new ContatoDAO();
        dao.queryContato(MainActivity.this);
        rvContatos = findViewById(R.id.rv_todos);
        semContato = findViewById(R.id.tv_sem_contatos);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
//                MainActivity.this.finish();
            startActivity(intent);
        });
    }

    public void showLoading(){
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.carregando), true);
    }

    public void hideLoading(){
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onSuccessRemove() {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.contato_excluido), Toast.LENGTH_SHORT).show();
        showLoading();
        dao.queryContato(MainActivity.this);
    }

    @Override
    public void onSuccessList(List<Contato> lista) {
        hideLoading();

        contatoList = lista;
        ContatoAdapter contatoAdapter = new ContatoAdapter(dao, contatoList, this);
        rvContatos.setLayoutManager(new LinearLayoutManager(this));
        rvContatos.setAdapter(contatoAdapter);

        if(contatoList != null && contatoList.size() > 0){
            semContato.setVisibility(View.GONE);
            rvContatos.setVisibility(View.VISIBLE);
        }else{
            rvContatos.setVisibility(View.GONE);
            semContato.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String mensagemErro) {
        hideLoading();
        Toast.makeText(MainActivity.this, mensagemErro, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessQueryId(Contato contato) {
        //sem ação
    }

    @Override
    public void onSuccessSave() {
        //sem ação
    }

    @Override
    public void onSuccessUpdate() {
        //sem ação
    }
}
