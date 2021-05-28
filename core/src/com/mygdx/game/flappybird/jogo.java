package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class jogo extends ApplicationAdapter {


	//cria as variaveis e a lista
	private float larguraDispositivo;
	private float alturaDispositivo;
	private float posicaoInicialVerticalPassaro = 0;
	private float variacao = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;



	private int pontos = 0;
	private int estadoJogo =0;
	private float gravidade = 0;

	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	SpriteBatch batch;

	private boolean passouCano = false;
	private Random random;
	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	@Override
	//classe semelhante ao onCreate
	public void create () {

		inicializaTexturas();
		inicializaObjetos();


	}

	private void inicializaObjetos() {
		random = new Random();
		batch = new SpriteBatch();

		//pega a altura e largura da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo/2;
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 300;

		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(3);

		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(3);

		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();


	}

	private void inicializaTexturas() {
		//cria uma lista de objetos passaros e atribui os tres a ela
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//pega a imagem fundo e atribui a variavel
		fundo = new Texture("fundo.png");
		//pega as texturas dodos canos e atreibui a variavel
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		gameOver = new Texture("game_over.png");
	}

	@Override
	//renderiza o conteudo
	public void render () {

		verificaEstadojogo();
		desenharTexturas();
		detectarColisao();
		validaPontos();

	}

	private void detectarColisao() {
		//cria os colliders
		circuloPassaro.set(50 + passaros[0].getWidth()/2,
				posicaoInicialVerticalPassaro+passaros[0].getHeight()/2,passaros[0].getHeight());

		retanguloCanoBaixo.set(posicaoCanoHorizontal,
				alturaDispositivo/2-canoBaixo.getHeight()-espacoEntreCanos/2+posicaoCanoVertical,canoBaixo.getWidth(),
				canoBaixo.getHeight());

		retanguloCanoCima.set(posicaoCanoHorizontal,
				alturaDispositivo/2 + espacoEntreCanos/2 + posicaoCanoVertical,
				canoTopo.getWidth(),canoTopo.getHeight());
			//cria os booleans para verificar a colisão
			boolean bateuCanoCima = Intersector.overlaps(circuloPassaro,retanguloCanoCima);
			boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro,retanguloCanoBaixo);

			//verifica se bateu
			if(bateuCanoBaixo || bateuCanoCima){
				Gdx.app.log("tag","bateu");
				estadoJogo=2;
			}


	}

		//verifica se passou do cano se sim adiciona mais um ao pontos
	private void validaPontos() {
		if (posicaoCanoHorizontal<50-passaros[0].getWidth())
		{
			if(!passouCano)
			{
				pontos++;
				passouCano = true;
			}
		}

		variacao += Gdx.graphics.getDeltaTime() * 10;
		//verifica se a variação e se for maior que tres iguala a zero
		if(variacao > 3)
			variacao = 0;
	}

	private void verificaEstadojogo() {

		//verificação do clique na tela
		boolean toquTela = Gdx.input.justTouched();

		if(estadoJogo==0){

			//se flor clicado subtrai 25 da gravidade
			if(toquTela)
			{
				gravidade = -15;
				estadoJogo =1;
			}

		}else if(estadoJogo ==1){

			//se flor clicado subtrai 25 da gravidade
			if(toquTela)
			{
				gravidade = -15;
			}

			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime()*200;
			if (posicaoCanoHorizontal < - canoBaixo.getHeight()){
				posicaoCanoHorizontal = larguraDispositivo;
				posicaoCanoVertical = random.nextInt(400)-200;
				passouCano = false;
			}

			//verifica se a posição vertical é maior que zero ou clicou na tela
			if (posicaoInicialVerticalPassaro > 0 || toquTela) {
				//atualiza a posição vertical
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
			}

			//adiciona +1 nas variaveis
			gravidade++;

		}else if(estadoJogo ==2){

		}


	}


	private void desenharTexturas() {
		batch.begin();

		//desenha e configura o onbeto na tela
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],50,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo/2- canoBaixo.getHeight()-espacoEntreCanos/2+ posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos/2 + posicaoCanoVertical);
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2,alturaDispositivo-100);

		if(estadoJogo==2){
			batch.draw(gameOver,larguraDispositivo/2 - gameOver.getWidth()/2,alturaDispositivo/2);
			textoReiniciar.draw(batch,"TOQUE NA TELA PARA REINICIAR!",larguraDispositivo/2 - 200,alturaDispositivo/2-gameOver.getHeight()/2);
			textoMelhorPontuacao.draw(batch,"SUA MELHOR PONTUAÇÃOÉ :0 PONTOS",larguraDispositivo/2 - 300 ,alturaDispositivo/2-gameOver.getHeight()*2);
		}


		batch.end();
	}

	@Override
	public void dispose () {

	}
}
