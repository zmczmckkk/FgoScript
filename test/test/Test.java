package test;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {

		new Thread() {
			public void run() {
				new InsertData().insert(Thread.currentThread());
			}
		}.start();

		new Thread() {
			public void run() {
				new InsertData().insert(Thread.currentThread());
			}
		}.start();
	}
}
class Test2 {
	public static void main(String[] args) {
//		
//		final Object lock = new Object();
//		Callable<String> ca = new Callable<String>() {
//			public String call() throws Exception {
//				new Service().testMethod(lock);
//				return null;
//			}
//		};
//
//		ExecutorService executorService = Executors.newSingleThreadExecutor();
//		Future<String> future = executorService.submit(ca);
//
//
//	try {
//			future.get(2, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			System.out.println("终止了颜色检测程序！");
//		} catch (TimeoutException e) {
//			/*synchronized (lock) {
//				lock.notifyAll();
//			}*/
//			future.cancel(true);
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//
//		} finally {
//			executorService.shutdown();
//		}
//	executorService = Executors.newSingleThreadExecutor();
//	future = executorService.submit(ca);
//	try {
//		future.get(2, TimeUnit.SECONDS);
//	} catch (InterruptedException e) {
//		System.out.println("终止了颜色检测程序！");
//	} catch (TimeoutException e) {
//		/*synchronized (lock) {
//			lock.notifyAll();
//		}*/
//		future.cancel(true);
//		e.printStackTrace();
//	} catch (ExecutionException e) {
//		e.printStackTrace();
//
//	} finally {
//		executorService.shutdown();
//	}
//	
	}
}

class InsertData {
	private  ArrayList<Integer> arrayList = new ArrayList<Integer>();
	private static Object ob= new Object();
	public  synchronized void insert(Thread thread) {
		synchronized (ob) {
			for (int i = 0; i < 5; i++) {
				System.out.println(thread.getName() + "在插入数据" + i);
				arrayList.add(i);
			}
		}
	}
}
