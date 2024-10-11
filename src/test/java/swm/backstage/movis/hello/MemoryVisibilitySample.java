package swm.backstage.movis.hello;


import org.junit.jupiter.api.Disabled;

// MemoryVisibility
@Disabled
public class MemoryVisibilitySample {
    private static volatile boolean flag = false;
    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // 약간의 지연을 추가하여 읽기 스레드보다 늦게 실행
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
            System.out.println("Writer thread updated the flag to true.");
        });

        Thread readerThread = new Thread(() -> {
            while (!flag) {
            }
            System.out.println("Reader thread detected that flag is true.");
        });

        readerThread.start();
        writerThread.start();
        writerThread.join();
        readerThread.join();

        System.out.println("Main thread finished.");
    }
}

// 1. lock 에 걸려있다
// 2. db에는 C가 저장