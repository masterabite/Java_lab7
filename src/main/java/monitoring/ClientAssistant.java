package monitoring;

import commands.*;
import mynet.Container;
import io.Message;
import io.Reader;
import mynet.User;
import stored.Movie;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Класс асистента который реализует взаимодействие с пользователем и работу комманд
 */
public class ClientAssistant {

    private Reader reader;

    private Socket socket;

    private Message serverMessage;

    private User user;

    /**
     * Хранит доступные комманды
     */
    private ArrayList<Command> commands;

    /**
     * переменная для завершения работы программы
     * fale- если работа завершена, true- иначе
     */
    private boolean execution;

    public ClientAssistant(Socket socket) {
        serverMessage = new Message();
        commands = new ArrayList<>();
        reader = new Reader();
        this.socket = socket;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * процедура ищет команду среди существующих и выполняет, при нахождении
     * @param string строка, содержащая имя команды
     * @param visible определяет нужно ли выводить саму команду (если ввод осуществляется из скрипта)
     */
    public void commitCommand(String string, boolean visible) throws IOException, ClassNotFoundException {
        Command command = findCommand(string);
        if (null == command) {
            System.out.println("К сожалению, я такой команды не знаю :(\nПопробуйте еще...");
        } else if (command.getName().equals("exit")) {
            this.stop();
        } else {
            Message args = new Message(2);
            if (command.getName().equals("add") || command.getName().equals("add_if_min")) {
                Movie movie = reader.readMovie(new Movie(user), false);
                args.add(movie.toCSV(), 0);
            }

            if (command.getName().equals("count_by_total_box_office") || command.getName().equals("remove_by_id")
                    || command.getName().equals("remove_lower")) {
                Integer number = reader.readInteger("Введите число: ", false);
                args.add(number.toString(), 1);
            }

            if (command.getName().equals("update_by_id")) {
                args.add(reader.readInteger("Введите id: ", false).toString(), 1);
                Movie movie = reader.readMovie(new Movie(), false);
                args.add(movie.toCSV(), 0);
            }

            if (command.getName().equals("execute_script")) {
                args.add(reader.readLine("Введите имя скрипта: ", false), 1);
            }

            if (command.getName().equals("filter_contains_name")) {
                args.add(reader.readLine("Введите подстроку: ", false), 1);
            }

            dataExchange(new Container(command, args, user)).print();
        }
    }

    /**
     * процедура добавляет все доступные комманды, и начинает интерактивное общение с пользователем
     */
    public void start() {

        while (true) {
            int type = reader.readAorR(false);
            user = new User(
                    reader.readLine("Введите логин: ", false),
                    reader.readPassword("Введите пароль: ", false)
            );

            try {
                Message serverMessage = dataExchange(new Container(user, type));
                if (serverMessage.getAt(0).equals("Ok")) {
                    System.out.println("Успешная авторизация...");
                    break;
                } else {
                    serverMessage.print();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.execution = true;
        this.commands.add(new HelpCommand());this.commands.add(new InfoCommand());
        this.commands.add(new ShowCommand());this.commands.add(new AddCommand());
        this.commands.add(new UpdateByIdCommand());this.commands.add(new RemoveByIdCommand());
        this.commands.add(new ClearCommand());this.commands.add(new ExecuteScriptCommand());
        this.commands.add(new ExitCommand());this.commands.add(new AddIfMinCommand());
        this.commands.add(new RemoveLowerCommand());this.commands.add(new HistoryCommand());
        this.commands.add(new MaxByTotalBoxOfficeCommand());
        this.commands.add(new CountByTotalBoxOfficeCommand());
        this.commands.add(new FilterContainsNameCommand());

        System.out.println("help- получить список комманд");

        while (this.execution) {
            try {
                String commandName = this.reader.readLine("Введите комманду: ", false);
                commitCommand(commandName, false);
            } catch (NoSuchElementException e) {
                System.out.println("Скрипт поврежден.");
                reader.getConsoleScan().close();
                reader.setConsoleScan(new Scanner(System.in));
            } catch (ClassNotFoundException e) {
                System.out.println("Не удалось получить сообщения от сервера.");
            } catch ( IOException e) {
                System.out.println("Потеряно соединение с сервером.");
            }
        }
    }

    /**
     * @return возвращает команду, имя которой эквивалентно данному и
     * null если такой команды нет
     * @param name имя искомой команды
     */
    public Command findCommand(String name) {
        for (Command currentCommand: commands) {
            if (currentCommand.getName().equals(name)) {
                return currentCommand;
            }
        }
        return null;
    }


    public Message dataExchange(Container container) throws IOException, ClassNotFoundException {
        System.out.println("Отправка данных...");
        socket.getOutputStream().write(Control.serialize(container));

        System.out.println("Ожидание ответа от сервера...");
        byte[] buffer = new byte[10000];
        socket.getInputStream().read(buffer);
        serverMessage = (Message) Control.deserialize(buffer);
        System.out.println("Ответ получен.");
        return serverMessage;
    }

    /**
     * функция прекращает работу асистента
     */
    public void stop() throws IOException {
        this.execution = false;
        socket.close();
        System.out.println("Завершение...");
        System.exit(0);
    }

}
