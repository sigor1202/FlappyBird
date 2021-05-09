package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//cria as variaveis e a lista
	private int movimentaY=0;
	private  int movimentax =0;
	SpriteBatch batch;
	Texture[] passaros;
	Texture fundo;

	private float larguraDispositivo;
	private float alturaDispositivo;
	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;

	@Override
	//classe semelhante ao onCreate
	public void create () {
		batch = new SpriteBatch();
		//cria uma lista de objetos passaros e atribui os tres a ela
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//pega a imagem fundo e atribui a variavel
		fundo = new Texture("fundo.png");

		//pega a altura e largura da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo/2;

	}

	@Override
	//renderiza o conteudo
	public void render () {

		batch.begin();

		//verifica se a variação e se for maior que tres iguala a zero
		if(variacao > 3)
			variacao = 0;

		//verificação do clique na tela
		boolean toquTela = Gdx.input.justTouched();

		//se flor clicado subtrai 25 da gravidade
		if(Gdx.input.justTouched())
		{
			gravidade = -25;
		}

		//verifica se a posição vertical é maior que zero ou clicou na tela
		if (posicaoInicialVerticalPassaro > 0 || toquTela)
			//atualiza a posição vertical
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		//desenha e configura o onjeto na tela
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],30,posicaoInicialVerticalPassaro);

		variacao += Gdx.graphics.getDeltaTime() * 10;

		//adiciona +1 nas variaveis
		gravidade++;
		movimentax++;
		movimentaY++;

		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
