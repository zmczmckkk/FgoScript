package test;

import fgoScript.entity.guda.ApGudaziFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApGudaziFactoryTest {

    @Test
    public void getInstance() {
        try {
            ApGudaziFactory.getInstance("train", "small", null).startAllFgo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}