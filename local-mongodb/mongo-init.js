db = db.getSiblingDB('products');


db.createUser({
     user: 'dev_mongo',
     pwd: 'dev_password',
     roles: ['readWrite'],
});
