package monitoring;

import commands.*;
import exceptions.*;
import mynet.Container;
import io.Message;
import io.Reader;
import mynet.DB;
import mynet.User;
import stored.Movie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс асистента который реализует взаимодействие с пользователем и работу комманд
 */
public class ServerAssistant {
    /**
     * Наша коллекция
     */
    private HashSet<Movie> movies;

    private Message clientMessage;

    /**
     * Дата инициализации коллекции
     */
    private Date moviesInitializationDate;

    /**
     * файл для считывания и записи
     */
    private File myFile;

    private Reader reader;

    /**
     * Пул потоков для чтения запросов
     */
    private static ExecutorService readThreadPool = Executors.newCachedThreadPool();


    /**
     * Пул потоков для обработки запросов и отправки ответа
     */
        public static ExecutorService pwThreadPool = Executors.newFixedThreadPool(2);

    private ServerSocketChannel channel;
    private Selector selector;
    /**
     * компаратор для сравнения элементов коллекции по принципу сравнения их hashCode)
     */
    private Comparator<Movie> movieComparator = Comparator.comparing(Movie::hashCode);

    /**
     * объек для записи коллекции в файл
     */
    private BufferedWriter bw;

    /**
     * Хранить список выполненных комманд
     */
    private ArrayList<String> history;

    /**
     * Максимальные сборы среди всех элементов в коллекции
     */
    private Long maxTotalBoxOffice;

    /**
     * Хранит доступные комманды
     */
    private ArrayList<Command> commands;

    private DB db;

    private User currentUser;

    /**
     * переменная для завершения работы программы
     * fale- если работа завершена, true- иначе
     */
    private boolean execution;

    public ServerAssistant(String[] args, ServerSocketChannel channel, Selector selector) throws SQLException {

        this.clientMessage = new Message();
        this.execution = false;
        this.channel = channel;
        this.selector = selector;

        maxTotalBoxOffice = -1L;
        history = new ArrayList<>();

        moviesInitializationDate = new Date();

        if (args.length > 0) {
            myFile = new File(args[0]);
        }

        reader = new Reader(myFile, this);
        db = new DB();

        movies = db.getElements();

        if (reader.getScan() != null) {
            reader.getScan().close();
        }
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public DB getDb() {
        return db;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public void setMovies(HashSet<Movie> movies) {
        this.movies = movies;
    }

    public void deleteMovie(Movie movie) {
        if (!db.deleteElement(movie)) {
            clientMessage.add("Не удалось удалить элемент.\n");
        }
    }

    public boolean addMovie(Movie movie) {
        boolean result = getDb().addElement(movie);
        if(result) {
            updateMovies();
            clientMessage.add("Элемент успешо добавлен.\n");
        } else {
            clientMessage.add("Элемент не удалось добавить.\n");
        }
        return result;
    }

    public Reader getReader() {
        return reader;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Date getMoviesInitializationDate() {
        return moviesInitializationDate;
    }

    /**
     * @return возвращает верхнюю строку для записи в файл
     */

    public File getMyFile() {
        return myFile;
    }

    public Comparator<Movie> getMovieComparator() {
        return movieComparator;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public HashSet<Movie> getMovies() {
        return movies;
    }


    public void commitCommand(Command command) {
        if (null == command) {
            System.out.println("Команда неопознана.");
        } else {
            this.history.add(command.getName());

            ReentrantLock rl = new ReentrantLock();

            try {
                rl.lockInterruptibly();
                try {
                    command.commit(this, true);
                } finally {
                    rl.unlock();
                }
            }catch (InterruptedException e) {
                System.err.println("Interrupted wait");
            }
        }
    }

    /**
     * процедура добавляет все доступные комманды, и начинает интерактивное общение с пользователем
     */
    public void start() {
        commands = new ArrayList<>();
        commands.add(new HelpCommand());
        commands.add(new InfoCommand());
        commands.add(new ShowCommand());
        commands.add(new AddCommand());
        commands.add(new UpdateByIdCommand());
        commands.add(new RemoveByIdCommand());
        commands.add(new ClearCommand());
        commands.add(new ExecuteScriptCommand());
        commands.add(new ExitCommand());
        commands.add(new AddIfMinCommand());
        commands.add(new RemoveLowerCommand());
        commands.add(new HistoryCommand());
        commands.add(new MaxByTotalBoxOfficeCommand());
        commands.add(new CountByTotalBoxOfficeCommand());
        commands.add(new FilterContainsNameCommand());

        sortMovies();
        this.execution = true;
        Object lock = new Object();

        while (this.execution) {
            try {
                selector.selectNow();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isAcceptable()) {
                            try {
                                accept();
                                System.out.println("Клиент подключен.");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (key.isReadable()) {

                            //задача чтения
                            Callable<Container> readTask = (() -> {
                                System.out.println("Чтение данных из канала...");
                                this.clientMessage = new Message();
                                return readContainer(key);
                            });

                            //добавление потока в пул
                            Future<Container> readFuture = readThreadPool.submit(readTask);

                            //периодически проверяем готовность
                            while (!readFuture.isDone()) {}

                            //обрабатываем полученый запрос
                            Runnable processTask = (()-> {
                                try {
                                    Container container = readFuture.get();
                                    if (container.getType() == 0) {
                                        Command currentCommand = container.getCommand();
                                        Message args = container.getMessage();
                                        System.out.println("Клиент отправляет комманду: " + currentCommand.getName());

                                        this.reader.setFileScan(new Scanner(args.getAt(0)));
                                        this.reader.setConsoleScan(new Scanner(args.getAt(1)));

                                        commitCommand(currentCommand);

                                    } else {
                                        currentUser = container.getUser();
                                        if (container.getType() == 1) {
                                            db.registerUser(currentUser, clientMessage);
                                        } else {
                                            db.loginUser(currentUser, clientMessage);
                                        }
                                    }
                                } catch (Exception e) {
                                    key.cancel();
                                    System.out.println("Соединение с клиентом прервано.");
                                    System.out.println("Ожидание подключения клиента...");
                                }
                            });

                            Future processFuture = pwThreadPool.submit(processTask);

                            while(!processFuture.isDone()) {}

                            key.interestOps(SelectionKey.OP_WRITE);

                        } else if (key.isWritable()) {

                            Runnable writeTask = (()-> {
                                try {
                                    System.out.println("Отправка ответа клиенту...");
                                    key.attach(this.clientMessage);
                                    writeMessage(key);
                                } catch (Exception e) {
                                    System.out.println("Соединение с клиентом прервано...");
                                }
                            });

                            Future writeFuture = pwThreadPool.submit(writeTask);

                            while (!writeFuture.isDone()) {}

                            key.interestOps(SelectionKey.OP_READ);
                        }
                        keyIterator.remove();
                }
            } catch (Exception e) {
                System.out.println("Соединение с клиентом прервано...");
            }
        }
    }

    public Message getClientMessage() {
        return this.clientMessage;
    }

    /**
     * @return возвращает элемент коллекции, id котрого равен данному и
     * null если такого элемента в коллекции нет
     * @param id id искомого объекта
     */
    public Movie findMovie(int id) {
        for(Movie movie: movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }

    /**
     * функция возвращает элемент коллекции, id котрого равен данному и
     * null если такого элемента в коллекции нет
     */
    public void sortMovies() {
        List<Movie> list = new ArrayList<>(movies);
        list.sort(movieComparator);
        movies = new HashSet<>(list);

    }

    public boolean checkMovieMaster(Movie movie) {
        if (movie == null) {
            return false;
        }
       if (movie.getMaster().equals(currentUser.getLogin())) {
           return true;
       } else {
           return false;
       }
    }

    /**
     * функция прекращает работу асистента
     */
    public void stop() {
        this.execution = false;
        System.out.println("Завершение...");
        System.exit(0);
    }


    public void accept() throws IOException{
        SocketChannel client = channel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public void updateMovies() {
        movies = db.getElements();
    }

    public Container readContainer(SelectionKey key) throws IOException, ClassNotFoundException{
        ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
        SocketChannel client = (SocketChannel) key.channel();
        client.read(byteBuffer);
        return (Container) Control.deserialize(byteBuffer.array());
    }

    public void writeMessage(SelectionKey key) throws Exception{
        Message message = (Message) key.attachment();
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.write(ByteBuffer.wrap(Control.serialize(message)));
    }
}
