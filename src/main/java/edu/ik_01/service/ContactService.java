package edu.ik_01.service;

import edu.ik_01.entity.Contact;

import java.util.List;

/**
 * Date: 27.08.15
 * Time: 17:22
 *
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 * @author http://mruslan.com
 */
public interface ContactService {

    Contact save(Contact contact);

    List<Contact> findAll();

}
