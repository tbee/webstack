package org.tbee.webstack.vdn.form;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.ValidationException;

abstract public class AbstractCrudFormLayout<E> extends FormLayout {
	abstract public AbstractCrudFormLayout<E> populateWith(E item);
	abstract public AbstractCrudFormLayout<E> writeTo(E item) throws ValidationException;
}