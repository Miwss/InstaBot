package ru.betry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App{
	public final static void main(String[] args){
		String TOKEN = "1720737287:AAGG4TcBpxbUp9BZWNOJrRC-F-SwjzjaIXQ"; //Записываем токен в переменную
		//ID		Polzovatel
		Map<Integer, User> users = new HashMap<>();

		TelegramBot bot = new TelegramBot(TOKEN); //Создаем новый обьект бота с нашим токеном


		bot.setUpdatesListener(updates -> { //Получаем доступ ко всем обновлениям бота

			updates.forEach(System.out::println);//через forEach выводим все обновления полученные от бота
			//логин и пароль
			//изображение
			//текст под изображением
			//геопозиция
			updates.forEach(update -> { //Обращаемся к пользователю, пробегаемся каждый раз по нему
				Integer userId = update.message().from().id(); //Получаем айди пользователя через месседж.фром.айди
				if(!users.containsKey(userId)){ //Проверяем на наличие уже существующего пользователя
					bot.execute(new SendMessage(update.message().chat().id(), "Вам необходимо прислать логин и пароль в одном предложении через пробел!"));//Если его нет, то присылаем сообщение
					users.put(userId, null);//Добавляем юзера в мапу, но с нулл значением для авторизации, добавляем айди
					//Если же у нас есть ключи, то делаем следующее
					//zapis login and pass
				}else if(users.get(userId)==null){//Проверяем на то что, юзер не прислал нам пароль и логин
						String[] loginAndPassword =  update.message().text().split(" ");//Именно тут мы берем пароль и логин пользователя и по пробелу разделяем их.
						User user = new User(loginAndPassword[0], loginAndPassword[1]);//создаем нового пользователя и кладем все в массив
						users.put(userId, user);//Добавляем нашего пользователя в мапу
						for(String s: loginAndPassword){//Проходимся циклом и получаем введеные пароли и логины
							System.out.println(s);
						}
						bot.execute(new SendMessage(update.message().chat().id(),"Все работает! Теперь вы можете присылать нам текст/изображение/геопозиция для Инстаграм (В одном сообщении)"));//Выводим сообщение об успешнол логине и просим внести данные об посте

				}else if(update.message().photo().length > 0){
						System.out.println(update.toString());
						//Далее создадим пост для работы основного функционала, пароли будем хранить в файлах
						//Post post = new Post();
						//post.setTitle(update.message().text());
						GetFileResponse fileResponse = bot.execute(new GetFile(update.message().photo()[0].fileId()));
						String fullPath =  bot.getFullFilePath(fileResponse.file());//fileResponse.file();
						//String fullPath = bot.getFullFilePath(file);
						System.out.println(fullPath);
						try{
							HttpDownload.downloadFile(fullPath, "./imageTest", update.message().photo()[0].fileId() + ".jpg");
						} catch (IOException e){
							System.err.println(e.getMessage());
						}
						Post post = new Post();
						post.setTitle(update.message().text());
						//post.setPhoto(new File("./imageTest", update.message().photo()[0].fileId() + ".jpg").getPath());
						users.get(userId).addPost(post);
						System.out.println(users.toString());
					}
			});
			return UpdatesListener.CONFIRMED_UPDATES_ALL;//возвращаем статус прочитаных сообщений, если статус none - не прочитано
		});

	}
}
