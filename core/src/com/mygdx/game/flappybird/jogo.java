package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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
	private float posicaoHorizontalPassaro=0;
	private float espacoEntreCanos;



	private int pontos = 0;
	private int pontuacaoMaxima=0;
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
	//cria o formato dos colliders
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	//cria os BitMaps
	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	//variaveis para os sons
	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;
	//cria a variavel de preferencias
	Preferences preferencias;

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
		//seta a altura do passaro na vertical
		posicaoInicialVerticalPassaro = alturaDispositivo/2;
		posicaoCanoHorizontal = larguraDispositivo;
		//seta o espa??o entre os canas
		espacoEntreCanos = 300;

		//cria e configura e texto da pontua????o
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);
		//cria e configura e texto de reiniciar
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(3);
		//cria e configura e texto melhor pontua????o
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(3);
		//cria os objetos para o collider
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
		//seta os nons nas variaveis
		somVoando= Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
		//configura as preferencias
		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima",0);

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
		//configura os colliders
		circuloPassaro.set(50 + passaros[0].getWidth()/2,
				posicaoInicialVerticalPassaro+passaros[0].getHeight()/2,passaros[0].getHeight());

		retanguloCanoBaixo.set(posicaoCanoHorizontal,
				alturaDispositivo/2-canoBaixo.getHeight()-espacoEntreCanos/2+posicaoCanoVertical,canoBaixo.getWidth(),
				canoBaixo.getHeight());

		retanguloCanoCima.set(posicaoCanoHorizontal,
				alturaDispositivo/2 + espacoEntreCanos/2 + posicaoCanoVertical,
				canoTopo.getWidth(),canoTopo.getHeight());
			//cria os booleans para verificar a colis??o
			boolean bateuCanoCima = Intersector.overlaps(circuloPassaro,retanguloCanoCima);
			boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro,retanguloCanoBaixo);

			//verifica se bateu
			if(bateuCanoBaixo || bateuCanoCima){
				if(estadoJogo==1){
					somColisao.play();
					estadoJogo=2;
				}
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
				somPontuacao.play();
			}
		}

		variacao += Gdx.graphics.getDeltaTime() * 10;
		//verifica se a varia????o e se for maior que tres iguala a zero
		if(variacao > 3)
			variacao = 0;
	}

	private void verificaEstadojogo() {

		//verifica????o do clique na tela
		boolean toquTela = Gdx.input.justTouched();
		//se o estado do jogo for 0
		if(estadoJogo==0){

			//se for clicado subtrai 15 da gravidade
			if(toquTela)
			{
				gravidade = -15;
				estadoJogo =1;
				somVoando.play();
			}
		//se o estado do jogo for 1
		}else if(estadoJogo ==1){

			//se flor clicado subtrai 25 da gravidade
			if(toquTela)
			{
				gravidade = -15;
				somVoando.play();
			}
			//faz o cano se mover
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime()*400;
			if (posicaoCanoHorizontal < - canoBaixo.getHeight()){
				posicaoCanoHorizontal = larguraDispositivo;
				posicaoCanoVertical = random.nextInt(400)-200;
				passouCano = false;
			}

			//verifica se a posi????o vertical ?? maior que zero ou clicou na tela
			if (posicaoInicialVerticalPassaro > 0 || toquTela) {
				//atualiza a posi????o vertical
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
			}

			//adiciona +1 nas variaveis
			gravidade++;

		}else if(estadoJogo ==2){
			//se os pontos for maior que a pontua????o maxima
			if(pontos>pontuacaoMaxima){
				//iguala a pontua????o maxima aos pontos
				pontuacaoMaxima = pontos;
				//retorna o valor da pontua????o maxima as preferencias
				preferencias.putInteger("pontuacaoMaxima",pontuacaoMaxima);
			}

			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime()*500;

			if(toquTela){
				estadoJogo=0;
				pontos = 0;
				gravidade=0;
				posicaoHorizontalPassaro =0;
				posicaoInicialVerticalPassaro = alturaDispositivo/2;
				posicaoCanoHorizontal = larguraDispositivo;
			}

		}


	}


	private void desenharTexturas() {
		batch.begin();

		//desenha e configura o onbeto na tela
		batch.draw(fundo, 0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao],50+posicaoHorizontalPassaro,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo/2- canoBaixo.getHeight()-espacoEntreCanos/2+ posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos/2 + posicaoCanoVertical);
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo/2,alturaDispositivo-100);
		//se o estado deo jogo for igual a 2
		if(estadoJogo==2){
			//desnha na tela o game over
			batch.draw(gameOver,larguraDispositivo/2 - gameOver.getWidth()/2,alturaDispositivo/2);
			//desenha na teala a frase TOQUE NA TELA PARA REINICIAR
			textoReiniciar.draw(batch,"TOQUE NA TELA PARA REINICIAR!",larguraDispositivo/2 - 200,alturaDispositivo/2-gameOver.getHeight()/2);
			// desenha a frase da melhor pontua????o e concatena o valor
			textoMelhorPontuacao.draw(batch,"SUA MELHOR PONTUA????O?? :"+pontuacaoMaxima+ "PONTOS",larguraDispositivo/2 - 300 ,alturaDispositivo/2-gameOver.getHeight()*2);
		}


		batch.end();
	}

	@Override
	public void dispose () {

	}
}
