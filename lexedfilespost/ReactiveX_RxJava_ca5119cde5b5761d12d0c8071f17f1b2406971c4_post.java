












package	io	.	reactivex	;	

import static	org	.	junit	.	Assert	.	*	;	
import static	org	.	mockito	.	ArgumentMatchers	.	any	;	
import static	org	.	mockito	.	Mockito	.	mock	;	

import	java	.	lang	.	reflect	.	*	;	
import	java	.	util	.	*	;	
import	java	.	util	.	concurrent	.	*	;	
import	java	.	util	.	concurrent	.	atomic	.	AtomicInteger	;	

import	org	.	mockito	.	Mockito	;	
import	org	.	mockito	.	invocation	.	InvocationOnMock	;	
import	org	.	mockito	.	stubbing	.	Answer	;	
import	org	.	reactivestreams	.	*	;	

import	io	.	reactivex	.	disposables	.	*	;	
import	io	.	reactivex	.	exceptions	.	*	;	
import	io	.	reactivex	.	functions	.	*	;	
import	io	.	reactivex	.	internal	.	functions	.	ObjectHelper	;	
import	io	.	reactivex	.	internal	.	fuseable	.	*	;	
import	io	.	reactivex	.	internal	.	operators	.	completable	.	CompletableToFlowable	;	
import	io	.	reactivex	.	internal	.	operators	.	maybe	.	MaybeToFlowable	;	
import	io	.	reactivex	.	internal	.	operators	.	single	.	SingleToFlowable	;	
import	io	.	reactivex	.	internal	.	subscriptions	.	BooleanSubscription	;	
import	io	.	reactivex	.	internal	.	util	.	ExceptionHelper	;	
import	io	.	reactivex	.	observers	.	TestObserver	;	
import	io	.	reactivex	.	parallel	.	ParallelFlowable	;	
import	io	.	reactivex	.	plugins	.	RxJavaPlugins	;	
import	io	.	reactivex	.	processors	.	PublishProcessor	;	
import	io	.	reactivex	.	schedulers	.	Schedulers	;	
import	io	.	reactivex	.	subjects	.	Subject	;	
import	io	.	reactivex	.	subscribers	.	TestSubscriber	;	




public	enum	TestHelper	{	
;	





public	static	final	int	RACE_DEFAULT_LOOPS	=	2500	;	





public	static	final	int	RACE_LONG_LOOPS	=	10000	;	






@SuppressWarnings	(	"str"	)	
public	static	<	T	>	FlowableSubscriber	<	T	>	mockSubscriber	(	)	{	
FlowableSubscriber	<	T	>	w	=	mock	(	FlowableSubscriber	.	class	)	;	

Mockito	.	doAnswer	(	new	Answer	<	Object	>	(	)	{	
@Override	
public	Object	answer	(	InvocationOnMock	a	)	throws	Throwable	{	
Subscription	s	=	a	.	getArgument	(	0	)	;	
s	.	request	(	Long	.	MAX_VALUE	)	;	
return	null	;	
}	
}	)	.	when	(	w	)	.	onSubscribe	(	(	Subscription	)	any	(	)	)	;	

return	w	;	
}	






@SuppressWarnings	(	"str"	)	
public	static	<	T	>	Observer	<	T	>	mockObserver	(	)	{	
return	mock	(	Observer	.	class	)	;	
}	






@SuppressWarnings	(	"str"	)	
public	static	<	T	>	MaybeObserver	<	T	>	mockMaybeObserver	(	)	{	
return	mock	(	MaybeObserver	.	class	)	;	
}	






@SuppressWarnings	(	"str"	)	
public	static	<	T	>	SingleObserver	<	T	>	mockSingleObserver	(	)	{	
return	mock	(	SingleObserver	.	class	)	;	
}	





public	static	CompletableObserver	mockCompletableObserver	(	)	{	
return	mock	(	CompletableObserver	.	class	)	;	
}	






public	static	void	checkUtilityClass	(	Class	<	?	>	clazz	)	{	
try	{	
Constructor	<	?	>	c	=	clazz	.	getDeclaredConstructor	(	)	;	

c	.	setAccessible	(	true	)	;	

try	{	
c	.	newInstance	(	)	;	
fail	(	"str"	)	;	
}	catch	(	InvocationTargetException	ex	)	{	
assertEquals	(	"str"	,	ex	.	getCause	(	)	.	getMessage	(	)	)	;	
}	
}	catch	(	Exception	ex	)	{	
AssertionError	ae	=	new	AssertionError	(	ex	.	toString	(	)	)	;	
ae	.	initCause	(	ex	)	;	
throw	ae	;	
}	
}	

public	static	List	<	Throwable	>	trackPluginErrors	(	)	{	
final	List	<	Throwable	>	list	=	Collections	.	synchronizedList	(	new	ArrayList	<	Throwable	>	(	)	)	;	

RxJavaPlugins	.	setErrorHandler	(	new	Consumer	<	Throwable	>	(	)	{	
@Override	
public	void	accept	(	Throwable	t	)	{	
list	.	add	(	t	)	;	
}	
}	)	;	

return	list	;	
}	

public	static	void	assertError	(	List	<	Throwable	>	list	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	)	{	
Throwable	ex	=	list	.	get	(	index	)	;	
if	(	!	clazz	.	isInstance	(	ex	)	)	{	
AssertionError	err	=	new	AssertionError	(	clazz	+	"str"	+	list	.	get	(	index	)	)	;	
err	.	initCause	(	list	.	get	(	index	)	)	;	
throw	err	;	
}	
}	

public	static	void	assertUndeliverable	(	List	<	Throwable	>	list	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	)	{	
Throwable	ex	=	list	.	get	(	index	)	;	
if	(	!	(	ex	instanceof	UndeliverableException	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	list	.	get	(	index	)	)	;	
err	.	initCause	(	list	.	get	(	index	)	)	;	
throw	err	;	
}	
ex	=	ex	.	getCause	(	)	;	
if	(	!	clazz	.	isInstance	(	ex	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	clazz	+	"str"	+	list	.	get	(	index	)	)	;	
err	.	initCause	(	list	.	get	(	index	)	)	;	
throw	err	;	
}	
}	

public	static	void	assertError	(	List	<	Throwable	>	list	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	,	String	message	)	{	
Throwable	ex	=	list	.	get	(	index	)	;	
if	(	!	clazz	.	isInstance	(	ex	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	clazz	+	"str"	+	ex	)	;	
err	.	initCause	(	ex	)	;	
throw	err	;	
}	
if	(	!	ObjectHelper	.	equals	(	message	,	ex	.	getMessage	(	)	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	message	+	"str"	+	ex	.	getMessage	(	)	)	;	
err	.	initCause	(	ex	)	;	
throw	err	;	
}	
}	

public	static	void	assertUndeliverable	(	List	<	Throwable	>	list	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	,	String	message	)	{	
Throwable	ex	=	list	.	get	(	index	)	;	
if	(	!	(	ex	instanceof	UndeliverableException	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	list	.	get	(	index	)	)	;	
err	.	initCause	(	list	.	get	(	index	)	)	;	
throw	err	;	
}	
ex	=	ex	.	getCause	(	)	;	
if	(	!	clazz	.	isInstance	(	ex	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	clazz	+	"str"	+	list	.	get	(	index	)	)	;	
err	.	initCause	(	list	.	get	(	index	)	)	;	
throw	err	;	
}	
if	(	!	ObjectHelper	.	equals	(	message	,	ex	.	getMessage	(	)	)	)	{	
AssertionError	err	=	new	AssertionError	(	"str"	+	message	+	"str"	+	ex	.	getMessage	(	)	)	;	
err	.	initCause	(	ex	)	;	
throw	err	;	
}	
}	

public	static	void	assertError	(	TestObserver	<	?	>	to	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	)	{	
Throwable	ex	=	to	.	errors	(	)	.	get	(	0	)	;	
try	{	
if	(	ex	instanceof	CompositeException	)	{	
CompositeException	ce	=	(	CompositeException	)	ex	;	
List	<	Throwable	>	cel	=	ce	.	getExceptions	(	)	;	
assertTrue	(	cel	.	get	(	index	)	.	toString	(	)	,	clazz	.	isInstance	(	cel	.	get	(	index	)	)	)	;	
}	else	{	
fail	(	ex	.	toString	(	)	+	"str"	)	;	
}	
}	catch	(	AssertionError	e	)	{	
ex	.	printStackTrace	(	)	;	
throw	e	;	
}	
}	

public	static	void	assertError	(	TestSubscriber	<	?	>	ts	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	)	{	
Throwable	ex	=	ts	.	errors	(	)	.	get	(	0	)	;	
if	(	ex	instanceof	CompositeException	)	{	
CompositeException	ce	=	(	CompositeException	)	ex	;	
List	<	Throwable	>	cel	=	ce	.	getExceptions	(	)	;	
assertTrue	(	cel	.	get	(	index	)	.	toString	(	)	,	clazz	.	isInstance	(	cel	.	get	(	index	)	)	)	;	
}	else	{	
fail	(	ex	.	toString	(	)	+	"str"	)	;	
}	
}	

public	static	void	assertError	(	TestObserver	<	?	>	to	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	,	String	message	)	{	
Throwable	ex	=	to	.	errors	(	)	.	get	(	0	)	;	
if	(	ex	instanceof	CompositeException	)	{	
CompositeException	ce	=	(	CompositeException	)	ex	;	
List	<	Throwable	>	cel	=	ce	.	getExceptions	(	)	;	
assertTrue	(	cel	.	get	(	index	)	.	toString	(	)	,	clazz	.	isInstance	(	cel	.	get	(	index	)	)	)	;	
assertEquals	(	message	,	cel	.	get	(	index	)	.	getMessage	(	)	)	;	
}	else	{	
fail	(	ex	.	toString	(	)	+	"str"	)	;	
}	
}	

public	static	void	assertError	(	TestSubscriber	<	?	>	ts	,	int	index	,	Class	<	?	extends	Throwable	>	clazz	,	String	message	)	{	
Throwable	ex	=	ts	.	errors	(	)	.	get	(	0	)	;	
if	(	ex	instanceof	CompositeException	)	{	
CompositeException	ce	=	(	CompositeException	)	ex	;	
List	<	Throwable	>	cel	=	ce	.	getExceptions	(	)	;	
assertTrue	(	cel	.	get	(	index	)	.	toString	(	)	,	clazz	.	isInstance	(	cel	.	get	(	index	)	)	)	;	
assertEquals	(	message	,	cel	.	get	(	index	)	.	getMessage	(	)	)	;	
}	else	{	
fail	(	ex	.	toString	(	)	+	"str"	)	;	
}	
}	






public	static	<	E	extends	Enum	<	E	>	>	void	assertEmptyEnum	(	Class	<	E	>	e	)	{	
assertEquals	(	0	,	e	.	getEnumConstants	(	)	.	length	)	;	

try	{	
try	{	
Method	m0	=	e	.	getDeclaredMethod	(	"str"	)	;	

Object	[	]	a	=	(	Object	[	]	)	m0	.	invoke	(	null	)	;	
assertEquals	(	0	,	a	.	length	)	;	

Method	m	=	e	.	getDeclaredMethod	(	"str"	,	String	.	class	)	;	

m	.	invoke	(	"str"	)	;	
fail	(	"str"	)	;	
}	catch	(	InvocationTargetException	ex	)	{	
fail	(	ex	.	toString	(	)	)	;	
}	catch	(	IllegalAccessException	ex	)	{	
fail	(	ex	.	toString	(	)	)	;	
}	catch	(	IllegalArgumentException	ex	)	{	
}	
}	catch	(	NoSuchMethodException	ex	)	{	
fail	(	ex	.	toString	(	)	)	;	
}	
}	






public	static	void	assertBadRequestReported	(	Publisher	<	?	>	source	)	{	
List	<	Throwable	>	list	=	trackPluginErrors	(	)	;	
try	{	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

source	.	subscribe	(	new	FlowableSubscriber	<	Object	>	(	)	{	

@Override	
public	void	onSubscribe	(	Subscription	s	)	{	
try	{	
s	.	request	(	-	99	)	;	
s	.	cancel	(	)	;	
s	.	cancel	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	

@Override	
public	void	onNext	(	Object	t	)	{	

}	

@Override	
public	void	onError	(	Throwable	t	)	{	

}	

@Override	
public	void	onComplete	(	)	{	

}	

}	)	;	

try	{	
assertTrue	(	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	new	AssertionError	(	ex	.	getMessage	(	)	)	;	
}	

assertTrue	(	list	.	toString	(	)	,	list	.	get	(	0	)	instanceof	IllegalArgumentException	)	;	
assertEquals	(	"str"	,	list	.	get	(	0	)	.	getMessage	(	)	)	;	
}	finally	{	
RxJavaPlugins	.	setErrorHandler	(	null	)	;	
}	
}	









public	static	void	race	(	final	Runnable	r1	,	final	Runnable	r2	)	{	
race	(	r1	,	r2	,	Schedulers	.	single	(	)	)	;	
}	










public	static	void	race	(	final	Runnable	r1	,	final	Runnable	r2	,	Scheduler	s	)	{	
final	AtomicInteger	count	=	new	AtomicInteger	(	2	)	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	2	)	;	

final	Throwable	[	]	errors	=	{	null	,	null	}	;	

s	.	scheduleDirect	(	new	Runnable	(	)	{	
@Override	
public	void	run	(	)	{	
if	(	count	.	decrementAndGet	(	)	!	=	0	)	{	
while	(	count	.	get	(	)	!	=	0	)	{	}	
}	

try	{	
try	{	
r1	.	run	(	)	;	
}	catch	(	Throwable	ex	)	{	
errors	[	0	]	=	ex	;	
}	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	)	;	

if	(	count	.	decrementAndGet	(	)	!	=	0	)	{	
while	(	count	.	get	(	)	!	=	0	)	{	}	
}	

try	{	
try	{	
r2	.	run	(	)	;	
}	catch	(	Throwable	ex	)	{	
errors	[	1	]	=	ex	;	
}	
}	finally	{	
cdl	.	countDown	(	)	;	
}	

try	{	
if	(	!	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	{	
throw	new	AssertionError	(	"str"	)	;	
}	
}	catch	(	InterruptedException	ex	)	{	
throw	new	RuntimeException	(	ex	)	;	
}	
if	(	errors	[	0	]	!	=	null	&	&	errors	[	1	]	=	=	null	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	errors	[	0	]	)	;	
}	

if	(	errors	[	0	]	=	=	null	&	&	errors	[	1	]	!	=	null	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	errors	[	1	]	)	;	
}	

if	(	errors	[	0	]	!	=	null	&	&	errors	[	1	]	!	=	null	)	{	
throw	new	CompositeException	(	errors	)	;	
}	
}	







public	static	List	<	Throwable	>	compositeList	(	Throwable	ex	)	{	
if	(	ex	instanceof	UndeliverableException	)	{	
ex	=	ex	.	getCause	(	)	;	
}	
return	(	(	CompositeException	)	ex	)	.	getExceptions	(	)	;	
}	





public	static	void	assertNoOffer	(	SimpleQueue	<	?	>	q	)	{	
try	{	
q	.	offer	(	null	)	;	
fail	(	"str"	)	;	
}	catch	(	UnsupportedOperationException	ex	)	{	
}	
try	{	
q	.	offer	(	null	,	null	)	;	
fail	(	"str"	)	;	
}	catch	(	UnsupportedOperationException	ex	)	{	
}	
}	

@SuppressWarnings	(	"str"	)	
public	static	<	E	extends	Enum	<	E	>	>	void	checkEnum	(	Class	<	E	>	enumClass	)	{	
try	{	
Method	m	=	enumClass	.	getMethod	(	"str"	)	;	
m	.	setAccessible	(	true	)	;	
Method	e	=	enumClass	.	getMethod	(	"str"	,	String	.	class	)	;	
m	.	setAccessible	(	true	)	;	

for	(	Enum	<	E	>	o	:	(	Enum	<	E	>	[	]	)	m	.	invoke	(	null	)	)	{	
assertSame	(	o	,	e	.	invoke	(	null	,	o	.	name	(	)	)	)	;	
}	

}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	
}	








public	static	<	T	>	Consumer	<	TestSubscriber	<	T	>	>	subscriberSingleNot	(	final	T	value	)	{	
return	new	Consumer	<	TestSubscriber	<	T	>	>	(	)	{	
@Override	
public	void	accept	(	TestSubscriber	<	T	>	ts	)	throws	Exception	{	
ts	
.	assertSubscribed	(	)	
.	assertValueCount	(	1	)	
.	assertNoErrors	(	)	
.	assertComplete	(	)	;	

T	v	=	ts	.	values	(	)	.	get	(	0	)	;	
assertNotEquals	(	value	,	v	)	;	
}	
}	;	
}	








public	static	<	T	>	Consumer	<	TestObserver	<	T	>	>	observerSingleNot	(	final	T	value	)	{	
return	new	Consumer	<	TestObserver	<	T	>	>	(	)	{	
@Override	
public	void	accept	(	TestObserver	<	T	>	to	)	throws	Exception	{	
to	
.	assertSubscribed	(	)	
.	assertValueCount	(	1	)	
.	assertNoErrors	(	)	
.	assertComplete	(	)	;	

T	v	=	to	.	values	(	)	.	get	(	0	)	;	
assertNotEquals	(	value	,	v	)	;	
}	
}	;	
}	






public	static	void	doubleOnSubscribe	(	Subscriber	<	?	>	subscriber	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
BooleanSubscription	s1	=	new	BooleanSubscription	(	)	;	

subscriber	.	onSubscribe	(	s1	)	;	

BooleanSubscription	s2	=	new	BooleanSubscription	(	)	;	

subscriber	.	onSubscribe	(	s2	)	;	

assertFalse	(	s1	.	isCancelled	(	)	)	;	

assertTrue	(	s2	.	isCancelled	(	)	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	doubleOnSubscribe	(	Observer	<	?	>	subscriber	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d2	)	;	

assertFalse	(	d1	.	isDisposed	(	)	)	;	

assertTrue	(	d2	.	isDisposed	(	)	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	doubleOnSubscribe	(	SingleObserver	<	?	>	subscriber	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d2	)	;	

assertFalse	(	d1	.	isDisposed	(	)	)	;	

assertTrue	(	d2	.	isDisposed	(	)	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	doubleOnSubscribe	(	CompletableObserver	subscriber	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d2	)	;	

assertFalse	(	d1	.	isDisposed	(	)	)	;	

assertTrue	(	d2	.	isDisposed	(	)	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	doubleOnSubscribe	(	MaybeObserver	<	?	>	subscriber	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

subscriber	.	onSubscribe	(	d2	)	;	

assertFalse	(	d1	.	isDisposed	(	)	)	;	

assertTrue	(	d2	.	isDisposed	(	)	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDisposed	(	Flowable	<	T	>	source	)	{	
final	TestSubscriber	<	Object	>	ts	=	new	TestSubscriber	<	Object	>	(	0	L	)	;	
source	.	subscribe	(	new	FlowableSubscriber	<	Object	>	(	)	{	
@Override	
public	void	onSubscribe	(	Subscription	s	)	{	
ts	.	onSubscribe	(	new	BooleanSubscription	(	)	)	;	

s	.	cancel	(	)	;	

s	.	cancel	(	)	;	
}	

@Override	
public	void	onNext	(	Object	t	)	{	
ts	.	onNext	(	t	)	;	
}	

@Override	
public	void	onError	(	Throwable	t	)	{	
ts	.	onError	(	t	)	;	
}	

@Override	
public	void	onComplete	(	)	{	
ts	.	onComplete	(	)	;	
}	
}	)	;	
ts	.	assertEmpty	(	)	;	
}	





public	static	void	checkDisposed	(	Maybe	<	?	>	source	)	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	
source	.	subscribe	(	new	MaybeObserver	<	Object	>	(	)	{	

@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
try	{	
b	[	0	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	

b	[	1	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	

@Override	
public	void	onSuccess	(	Object	value	)	{	
}	

@Override	
public	void	onError	(	Throwable	e	)	{	
}	

@Override	
public	void	onComplete	(	)	{	
}	
}	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	
}	






public	static	void	checkDisposed	(	Observable	<	?	>	source	)	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	
source	.	subscribe	(	new	Observer	<	Object	>	(	)	{	

@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
try	{	
b	[	0	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	

b	[	1	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	

@Override	
public	void	onNext	(	Object	value	)	{	
}	

@Override	
public	void	onError	(	Throwable	e	)	{	
}	

@Override	
public	void	onComplete	(	)	{	
}	
}	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	
}	






public	static	void	checkDisposed	(	Single	<	?	>	source	)	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	
source	.	subscribe	(	new	SingleObserver	<	Object	>	(	)	{	

@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
try	{	
b	[	0	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	

b	[	1	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	

@Override	
public	void	onSuccess	(	Object	value	)	{	
}	

@Override	
public	void	onError	(	Throwable	e	)	{	
}	
}	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	
}	






public	static	void	checkDisposed	(	Completable	source	)	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	
source	.	subscribe	(	new	CompletableObserver	(	)	{	

@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
try	{	
b	[	0	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	

b	[	1	]	=	d	.	isDisposed	(	)	;	

d	.	dispose	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	

@Override	
public	void	onError	(	Throwable	e	)	{	
}	

@Override	
public	void	onComplete	(	)	{	
}	
}	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	
}	




enum	NoOpConsumer	implements	FlowableSubscriber	<	Object	>	,	Observer	<	Object	>	,	MaybeObserver	<	Object	>	,	SingleObserver	<	Object	>	,	CompletableObserver	{	
INSTANCE	;	

@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
}	

@Override	
public	void	onSuccess	(	Object	value	)	{	
}	

@Override	
public	void	onError	(	Throwable	e	)	{	
}	

@Override	
public	void	onComplete	(	)	{	
}	

@Override	
public	void	onSubscribe	(	Subscription	s	)	{	
}	

@Override	
public	void	onNext	(	Object	t	)	{	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeMaybe	(	Function	<	Maybe	<	T	>	,	?	extends	MaybeSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Maybe	<	T	>	source	=	new	Maybe	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	MaybeObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

MaybeSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeMaybeToSingle	(	Function	<	Maybe	<	T	>	,	?	extends	SingleSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Maybe	<	T	>	source	=	new	Maybe	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	MaybeObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

SingleSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeMaybeToObservable	(	Function	<	Maybe	<	T	>	,	?	extends	ObservableSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Maybe	<	T	>	source	=	new	Maybe	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	MaybeObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

ObservableSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeMaybeToFlowable	(	Function	<	Maybe	<	T	>	,	?	extends	Publisher	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Maybe	<	T	>	source	=	new	Maybe	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	MaybeObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

Publisher	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeSingleToMaybe	(	Function	<	Single	<	T	>	,	?	extends	MaybeSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Single	<	T	>	source	=	new	Single	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	SingleObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

MaybeSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeSingleToObservable	(	Function	<	Single	<	T	>	,	?	extends	ObservableSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Single	<	T	>	source	=	new	Single	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	SingleObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

ObservableSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeSingleToFlowable	(	Function	<	Single	<	T	>	,	?	extends	Publisher	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Single	<	T	>	source	=	new	Single	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	SingleObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

Publisher	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDoubleOnSubscribeMaybeToCompletable	(	Function	<	Maybe	<	T	>	,	?	extends	CompletableSource	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Maybe	<	T	>	source	=	new	Maybe	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	MaybeObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

CompletableSource	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeSingle	(	Function	<	Single	<	T	>	,	?	extends	SingleSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Single	<	T	>	source	=	new	Single	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	SingleObserver	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

SingleSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeFlowable	(	Function	<	Flowable	<	T	>	,	?	extends	Publisher	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Flowable	<	T	>	source	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	subscriber	)	{	
try	{	
BooleanSubscription	d1	=	new	BooleanSubscription	(	)	;	

subscriber	.	onSubscribe	(	d1	)	;	

BooleanSubscription	d2	=	new	BooleanSubscription	(	)	;	

subscriber	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isCancelled	(	)	;	
b	[	1	]	=	d2	.	isCancelled	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

Publisher	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeObservable	(	Function	<	Observable	<	T	>	,	?	extends	ObservableSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Observable	<	T	>	source	=	new	Observable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

ObservableSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeObservableToSingle	(	Function	<	Observable	<	T	>	,	?	extends	SingleSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Observable	<	T	>	source	=	new	Observable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

SingleSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeObservableToMaybe	(	Function	<	Observable	<	T	>	,	?	extends	MaybeSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Observable	<	T	>	source	=	new	Observable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

MaybeSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDoubleOnSubscribeObservableToCompletable	(	Function	<	Observable	<	T	>	,	?	extends	CompletableSource	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Observable	<	T	>	source	=	new	Observable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

CompletableSource	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeFlowableToObservable	(	Function	<	Flowable	<	T	>	,	?	extends	ObservableSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Flowable	<	T	>	source	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
try	{	
BooleanSubscription	d1	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

BooleanSubscription	d2	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isCancelled	(	)	;	
b	[	1	]	=	d2	.	isCancelled	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

ObservableSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeFlowableToSingle	(	Function	<	Flowable	<	T	>	,	?	extends	SingleSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Flowable	<	T	>	source	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
try	{	
BooleanSubscription	d1	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

BooleanSubscription	d2	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isCancelled	(	)	;	
b	[	1	]	=	d2	.	isCancelled	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

SingleSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	








public	static	<	T	,	R	>	void	checkDoubleOnSubscribeFlowableToMaybe	(	Function	<	Flowable	<	T	>	,	?	extends	MaybeSource	<	R	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Flowable	<	T	>	source	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
try	{	
BooleanSubscription	d1	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

BooleanSubscription	d2	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isCancelled	(	)	;	
b	[	1	]	=	d2	.	isCancelled	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

MaybeSource	<	R	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDoubleOnSubscribeFlowableToCompletable	(	Function	<	Flowable	<	T	>	,	?	extends	Completable	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Flowable	<	T	>	source	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
try	{	
BooleanSubscription	d1	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

BooleanSubscription	d2	=	new	BooleanSubscription	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isCancelled	(	)	;	
b	[	1	]	=	d2	.	isCancelled	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

Completable	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	checkDoubleOnSubscribeCompletable	(	Function	<	Completable	,	?	extends	CompletableSource	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Completable	source	=	new	Completable	(	)	{	
@Override	
protected	void	subscribeActual	(	CompletableObserver	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

CompletableSource	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDoubleOnSubscribeCompletableToMaybe	(	Function	<	Completable	,	?	extends	MaybeSource	<	T	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Completable	source	=	new	Completable	(	)	{	
@Override	
protected	void	subscribeActual	(	CompletableObserver	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

MaybeSource	<	T	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	>	void	checkDoubleOnSubscribeCompletableToSingle	(	Function	<	Completable	,	?	extends	SingleSource	<	T	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Completable	source	=	new	Completable	(	)	{	
@Override	
protected	void	subscribeActual	(	CompletableObserver	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

SingleSource	<	T	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	checkDoubleOnSubscribeCompletableToFlowable	(	Function	<	Completable	,	?	extends	Publisher	<	?	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Completable	source	=	new	Completable	(	)	{	
@Override	
protected	void	subscribeActual	(	CompletableObserver	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

Publisher	<	?	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	






public	static	void	checkDoubleOnSubscribeCompletableToObservable	(	Function	<	Completable	,	?	extends	ObservableSource	<	?	>	>	transform	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
final	Boolean	[	]	b	=	{	null	,	null	}	;	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

Completable	source	=	new	Completable	(	)	{	
@Override	
protected	void	subscribeActual	(	CompletableObserver	observer	)	{	
try	{	
Disposable	d1	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d1	)	;	

Disposable	d2	=	Disposables	.	empty	(	)	;	

observer	.	onSubscribe	(	d2	)	;	

b	[	0	]	=	d1	.	isDisposed	(	)	;	
b	[	1	]	=	d2	.	isDisposed	(	)	;	
}	finally	{	
cdl	.	countDown	(	)	;	
}	
}	
}	;	

ObservableSource	<	?	>	out	=	transform	.	apply	(	source	)	;	

out	.	subscribe	(	NoOpConsumer	.	INSTANCE	)	;	

try	{	
assertTrue	(	"str"	,	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertEquals	(	"str"	,	false	,	b	[	0	]	)	;	
assertEquals	(	"str"	,	true	,	b	[	1	]	)	;	

assertError	(	errors	,	0	,	IllegalStateException	.	class	,	"str"	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	







public	static	<	T	,	U	>	void	checkDisposedMaybe	(	Function	<	Maybe	<	T	>	,	?	extends	MaybeSource	<	U	>	>	composer	)	{	
PublishProcessor	<	T	>	pp	=	PublishProcessor	.	create	(	)	;	

TestSubscriber	<	U	>	ts	=	new	TestSubscriber	<	U	>	(	)	;	

try	{	
new	MaybeToFlowable	<	U	>	(	composer	.	apply	(	pp	.	singleElement	(	)	)	)	.	subscribe	(	ts	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertTrue	(	"str"	,	pp	.	hasSubscribers	(	)	)	;	

ts	.	cancel	(	)	;	

assertFalse	(	"str"	,	pp	.	hasSubscribers	(	)	)	;	
}	





public	static	void	checkDisposedCompletable	(	Function	<	Completable	,	?	extends	CompletableSource	>	composer	)	{	
PublishProcessor	<	Integer	>	pp	=	PublishProcessor	.	create	(	)	;	

TestSubscriber	<	Integer	>	ts	=	new	TestSubscriber	<	Integer	>	(	)	;	

try	{	
new	CompletableToFlowable	<	Integer	>	(	composer	.	apply	(	pp	.	ignoreElements	(	)	)	)	.	subscribe	(	ts	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertTrue	(	"str"	,	pp	.	hasSubscribers	(	)	)	;	

ts	.	cancel	(	)	;	

assertFalse	(	"str"	,	pp	.	hasSubscribers	(	)	)	;	
}	







public	static	<	T	,	U	>	void	checkDisposedMaybeToSingle	(	Function	<	Maybe	<	T	>	,	?	extends	SingleSource	<	U	>	>	composer	)	{	
PublishProcessor	<	T	>	pp	=	PublishProcessor	.	create	(	)	;	

TestSubscriber	<	U	>	ts	=	new	TestSubscriber	<	U	>	(	)	;	

try	{	
new	SingleToFlowable	<	U	>	(	composer	.	apply	(	pp	.	singleElement	(	)	)	)	.	subscribe	(	ts	)	;	
}	catch	(	Throwable	ex	)	{	
throw	ExceptionHelper	.	wrapOrThrow	(	ex	)	;	
}	

assertTrue	(	pp	.	hasSubscribers	(	)	)	;	

ts	.	cancel	(	)	;	

assertFalse	(	pp	.	hasSubscribers	(	)	)	;	
}	







public	static	void	assertCompositeExceptions	(	TestSubscriber	<	?	>	ts	,	Class	<	?	extends	Throwable	>	.	.	.	classes	)	{	
ts	
.	assertSubscribed	(	)	
.	assertError	(	CompositeException	.	class	)	
.	assertNotComplete	(	)	;	

List	<	Throwable	>	list	=	compositeList	(	ts	.	errors	(	)	.	get	(	0	)	)	;	

assertEquals	(	classes	.	length	,	list	.	size	(	)	)	;	

for	(	int	i	=	0	;	i	<	classes	.	length	;	i	+	+	)	{	
assertError	(	list	,	i	,	classes	[	i	]	)	;	
}	
}	








@SuppressWarnings	(	"str"	)	
public	static	void	assertCompositeExceptions	(	TestSubscriber	<	?	>	ts	,	Object	.	.	.	classes	)	{	
ts	
.	assertSubscribed	(	)	
.	assertError	(	CompositeException	.	class	)	
.	assertNotComplete	(	)	;	

List	<	Throwable	>	list	=	compositeList	(	ts	.	errors	(	)	.	get	(	0	)	)	;	

assertEquals	(	classes	.	length	,	list	.	size	(	)	)	;	

for	(	int	i	=	0	;	i	<	classes	.	length	;	i	+	=	2	)	{	
assertError	(	list	,	i	,	(	Class	<	Throwable	>	)	classes	[	i	]	,	(	String	)	classes	[	i	+	1	]	)	;	
}	
}	







public	static	void	assertCompositeExceptions	(	TestObserver	<	?	>	to	,	Class	<	?	extends	Throwable	>	.	.	.	classes	)	{	
to	
.	assertSubscribed	(	)	
.	assertError	(	CompositeException	.	class	)	
.	assertNotComplete	(	)	;	

List	<	Throwable	>	list	=	compositeList	(	to	.	errors	(	)	.	get	(	0	)	)	;	

assertEquals	(	classes	.	length	,	list	.	size	(	)	)	;	

for	(	int	i	=	0	;	i	<	classes	.	length	;	i	+	+	)	{	
assertError	(	list	,	i	,	classes	[	i	]	)	;	
}	
}	








@SuppressWarnings	(	"str"	)	
public	static	void	assertCompositeExceptions	(	TestObserver	<	?	>	to	,	Object	.	.	.	classes	)	{	
to	
.	assertSubscribed	(	)	
.	assertError	(	CompositeException	.	class	)	
.	assertNotComplete	(	)	;	

List	<	Throwable	>	list	=	compositeList	(	to	.	errors	(	)	.	get	(	0	)	)	;	

assertEquals	(	classes	.	length	,	list	.	size	(	)	)	;	

for	(	int	i	=	0	;	i	<	classes	.	length	;	i	+	=	2	)	{	
assertError	(	list	,	i	,	(	Class	<	Throwable	>	)	classes	[	i	]	,	(	String	)	classes	[	i	+	1	]	)	;	
}	
}	







public	static	<	T	>	void	emit	(	Processor	<	T	,	?	>	p	,	T	.	.	.	values	)	{	
for	(	T	v	:	values	)	{	
p	.	onNext	(	v	)	;	
}	
p	.	onComplete	(	)	;	
}	







public	static	<	T	>	void	emit	(	Subject	<	T	>	p	,	T	.	.	.	values	)	{	
for	(	T	v	:	values	)	{	
p	.	onNext	(	v	)	;	
}	
p	.	onComplete	(	)	;	
}	






public	static	<	T	>	void	checkFusedIsEmptyClear	(	Observable	<	T	>	source	)	{	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

final	Boolean	[	]	state	=	{	null	,	null	,	null	,	null	}	;	

source	.	subscribe	(	new	Observer	<	T	>	(	)	{	
@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
try	{	
if	(	d	instanceof	QueueDisposable	)	{	
@SuppressWarnings	(	"str"	)	
QueueDisposable	<	Object	>	qd	=	(	QueueDisposable	<	Object	>	)	d	;	
state	[	0	]	=	true	;	

int	m	=	qd	.	requestFusion	(	QueueFuseable	.	ANY	)	;	

if	(	m	!	=	QueueFuseable	.	NONE	)	{	
state	[	1	]	=	true	;	

state	[	2	]	=	qd	.	isEmpty	(	)	;	

qd	.	clear	(	)	;	

state	[	3	]	=	qd	.	isEmpty	(	)	;	
}	
}	
cdl	.	countDown	(	)	;	
}	finally	{	
d	.	dispose	(	)	;	
}	
}	

@Override	
public	void	onNext	(	T	value	)	{	

}	

@Override	
public	void	onError	(	Throwable	e	)	{	

}	

@Override	
public	void	onComplete	(	)	{	

}	
}	)	;	

try	{	
assertTrue	(	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	

assertTrue	(	"str"	,	state	[	0	]	)	;	
assertTrue	(	"str"	,	state	[	1	]	)	;	

assertNotNull	(	state	[	2	]	)	;	
assertTrue	(	"str"	,	state	[	3	]	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	new	RuntimeException	(	ex	)	;	
}	
}	






public	static	<	T	>	void	checkFusedIsEmptyClear	(	Flowable	<	T	>	source	)	{	
final	CountDownLatch	cdl	=	new	CountDownLatch	(	1	)	;	

final	Boolean	[	]	state	=	{	null	,	null	,	null	,	null	}	;	

source	.	subscribe	(	new	FlowableSubscriber	<	T	>	(	)	{	
@Override	
public	void	onSubscribe	(	Subscription	d	)	{	
try	{	
if	(	d	instanceof	QueueSubscription	)	{	
@SuppressWarnings	(	"str"	)	
QueueSubscription	<	Object	>	qd	=	(	QueueSubscription	<	Object	>	)	d	;	
state	[	0	]	=	true	;	

int	m	=	qd	.	requestFusion	(	QueueFuseable	.	ANY	)	;	

if	(	m	!	=	QueueFuseable	.	NONE	)	{	
state	[	1	]	=	true	;	

state	[	2	]	=	qd	.	isEmpty	(	)	;	

qd	.	clear	(	)	;	

state	[	3	]	=	qd	.	isEmpty	(	)	;	
}	
}	
cdl	.	countDown	(	)	;	
}	finally	{	
d	.	cancel	(	)	;	
}	
}	

@Override	
public	void	onNext	(	T	value	)	{	

}	

@Override	
public	void	onError	(	Throwable	e	)	{	

}	

@Override	
public	void	onComplete	(	)	{	

}	
}	)	;	

try	{	
assertTrue	(	cdl	.	await	(	5	,	TimeUnit	.	SECONDS	)	)	;	

assertTrue	(	"str"	,	state	[	0	]	)	;	
assertTrue	(	"str"	,	state	[	1	]	)	;	

assertNotNull	(	state	[	2	]	)	;	
assertTrue	(	"str"	,	state	[	3	]	)	;	
}	catch	(	InterruptedException	ex	)	{	
throw	new	RuntimeException	(	ex	)	;	
}	
}	






public	static	List	<	Throwable	>	errorList	(	TestObserver	<	?	>	to	)	{	
return	compositeList	(	to	.	errors	(	)	.	get	(	0	)	)	;	
}	






public	static	List	<	Throwable	>	errorList	(	TestSubscriber	<	?	>	ts	)	{	
return	compositeList	(	ts	.	errors	(	)	.	get	(	0	)	)	;	
}	











public	static	<	T	>	void	checkBadSourceObservable	(	Function	<	Observable	<	T	>	,	Object	>	mapper	,	
final	boolean	error	,	final	T	goodValue	,	final	T	badValue	,	final	Object	.	.	.	expected	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Observable	<	T	>	bad	=	new	Observable	<	T	>	(	)	{	
boolean	once	;	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
observer	.	onSubscribe	(	Disposables	.	empty	(	)	)	;	

if	(	once	)	{	
return	;	
}	
once	=	true	;	

if	(	goodValue	!	=	null	)	{	
observer	.	onNext	(	goodValue	)	;	
}	

if	(	error	)	{	
observer	.	onError	(	new	TestException	(	"str"	)	)	;	
}	else	{	
observer	.	onComplete	(	)	;	
}	

if	(	badValue	!	=	null	)	{	
observer	.	onNext	(	badValue	)	;	
}	
observer	.	onError	(	new	TestException	(	"str"	)	)	;	
observer	.	onComplete	(	)	;	
}	
}	;	

Object	o	=	mapper	.	apply	(	bad	)	;	

if	(	o	instanceof	ObservableSource	)	{	
ObservableSource	<	?	>	os	=	(	ObservableSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	Publisher	)	{	
Publisher	<	?	>	os	=	(	Publisher	<	?	>	)	o	;	
TestSubscriber	<	Object	>	ts	=	new	TestSubscriber	<	Object	>	(	)	;	

os	.	subscribe	(	ts	)	;	

ts	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

ts	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
ts	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
ts	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
ts	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	SingleSource	)	{	
SingleSource	<	?	>	os	=	(	SingleSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	MaybeSource	)	{	
MaybeSource	<	?	>	os	=	(	MaybeSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	CompletableSource	)	{	
CompletableSource	os	=	(	CompletableSource	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

assertUndeliverable	(	errors	,	0	,	TestException	.	class	,	"str"	)	;	
}	catch	(	AssertionError	ex	)	{	
throw	ex	;	
}	catch	(	Throwable	ex	)	{	
throw	new	RuntimeException	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	











public	static	<	T	>	void	checkBadSourceFlowable	(	Function	<	Flowable	<	T	>	,	Object	>	mapper	,	
final	boolean	error	,	final	T	goodValue	,	final	T	badValue	,	final	Object	.	.	.	expected	)	{	
List	<	Throwable	>	errors	=	trackPluginErrors	(	)	;	
try	{	
Flowable	<	T	>	bad	=	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
observer	.	onSubscribe	(	new	BooleanSubscription	(	)	)	;	

if	(	goodValue	!	=	null	)	{	
observer	.	onNext	(	goodValue	)	;	
}	

if	(	error	)	{	
observer	.	onError	(	new	TestException	(	"str"	)	)	;	
}	else	{	
observer	.	onComplete	(	)	;	
}	

if	(	badValue	!	=	null	)	{	
observer	.	onNext	(	badValue	)	;	
}	
observer	.	onError	(	new	TestException	(	"str"	)	)	;	
observer	.	onComplete	(	)	;	
}	
}	;	

Object	o	=	mapper	.	apply	(	bad	)	;	

if	(	o	instanceof	ObservableSource	)	{	
ObservableSource	<	?	>	os	=	(	ObservableSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	Publisher	)	{	
Publisher	<	?	>	os	=	(	Publisher	<	?	>	)	o	;	
TestSubscriber	<	Object	>	ts	=	new	TestSubscriber	<	Object	>	(	)	;	

os	.	subscribe	(	ts	)	;	

ts	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

ts	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
ts	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
ts	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
ts	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	SingleSource	)	{	
SingleSource	<	?	>	os	=	(	SingleSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	MaybeSource	)	{	
MaybeSource	<	?	>	os	=	(	MaybeSource	<	?	>	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

if	(	o	instanceof	CompletableSource	)	{	
CompletableSource	os	=	(	CompletableSource	)	o	;	
TestObserver	<	Object	>	to	=	new	TestObserver	<	Object	>	(	)	;	

os	.	subscribe	(	to	)	;	

to	.	awaitDone	(	5	,	TimeUnit	.	SECONDS	)	;	

to	.	assertSubscribed	(	)	;	

if	(	expected	!	=	null	)	{	
to	.	assertValues	(	expected	)	;	
}	
if	(	error	)	{	
to	.	assertError	(	TestException	.	class	)	
.	assertErrorMessage	(	"str"	)	
.	assertNotComplete	(	)	;	
}	else	{	
to	.	assertNoErrors	(	)	.	assertComplete	(	)	;	
}	
}	

assertUndeliverable	(	errors	,	0	,	TestException	.	class	,	"str"	)	;	
}	catch	(	AssertionError	ex	)	{	
throw	ex	;	
}	catch	(	Throwable	ex	)	{	
throw	new	RuntimeException	(	ex	)	;	
}	finally	{	
RxJavaPlugins	.	reset	(	)	;	
}	
}	

public	static	<	T	>	void	checkInvalidParallelSubscribers	(	ParallelFlowable	<	T	>	source	)	{	
int	n	=	source	.	parallelism	(	)	;	

@SuppressWarnings	(	"str"	)	
TestSubscriber	<	Object	>	[	]	tss	=	new	TestSubscriber	[	n	+	1	]	;	
for	(	int	i	=	0	;	i	<	=	n	;	i	+	+	)	{	
tss	[	i	]	=	new	TestSubscriber	<	Object	>	(	)	.	withTag	(	"str"	+	i	)	;	
}	

source	.	subscribe	(	tss	)	;	

for	(	int	i	=	0	;	i	<	=	n	;	i	+	+	)	{	
tss	[	i	]	.	assertFailure	(	IllegalArgumentException	.	class	)	;	
}	
}	

public	static	<	T	>	Observable	<	T	>	rejectObservableFusion	(	)	{	
return	new	Observable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	observer	)	{	
observer	.	onSubscribe	(	new	QueueDisposable	<	T	>	(	)	{	

@Override	
public	int	requestFusion	(	int	mode	)	{	
return	0	;	
}	

@Override	
public	boolean	offer	(	T	value	)	{	
throw	new	IllegalStateException	(	)	;	
}	

@Override	
public	boolean	offer	(	T	v1	,	T	v2	)	{	
throw	new	IllegalStateException	(	)	;	
}	

@Override	
public	T	poll	(	)	throws	Exception	{	
return	null	;	
}	

@Override	
public	boolean	isEmpty	(	)	{	
return	true	;	
}	

@Override	
public	void	clear	(	)	{	
}	

@Override	
public	void	dispose	(	)	{	
}	

@Override	
public	boolean	isDisposed	(	)	{	
return	false	;	
}	
}	)	;	
}	
}	;	
}	

public	static	<	T	>	Flowable	<	T	>	rejectFlowableFusion	(	)	{	
return	new	Flowable	<	T	>	(	)	{	
@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	observer	)	{	
observer	.	onSubscribe	(	new	QueueSubscription	<	T	>	(	)	{	

@Override	
public	int	requestFusion	(	int	mode	)	{	
return	0	;	
}	

@Override	
public	boolean	offer	(	T	value	)	{	
throw	new	IllegalStateException	(	)	;	
}	

@Override	
public	boolean	offer	(	T	v1	,	T	v2	)	{	
throw	new	IllegalStateException	(	)	;	
}	

@Override	
public	T	poll	(	)	throws	Exception	{	
return	null	;	
}	

@Override	
public	boolean	isEmpty	(	)	{	
return	true	;	
}	

@Override	
public	void	clear	(	)	{	
}	

@Override	
public	void	cancel	(	)	{	
}	

@Override	
public	void	request	(	long	n	)	{	
}	
}	)	;	
}	
}	;	
}	

static	final	class	FlowableStripBoundary	<	T	>	extends	Flowable	<	T	>	implements	FlowableTransformer	<	T	,	T	>	{	

final	Flowable	<	T	>	source	;	

FlowableStripBoundary	(	Flowable	<	T	>	source	)	{	
this	.	source	=	source	;	
}	

@Override	
public	Flowable	<	T	>	apply	(	Flowable	<	T	>	upstream	)	{	
return	new	FlowableStripBoundary	<	T	>	(	upstream	)	;	
}	

@Override	
protected	void	subscribeActual	(	Subscriber	<	?	super	T	>	s	)	{	
source	.	subscribe	(	new	StripBoundarySubscriber	<	T	>	(	s	)	)	;	
}	

static	final	class	StripBoundarySubscriber	<	T	>	implements	FlowableSubscriber	<	T	>	,	QueueSubscription	<	T	>	{	

final	Subscriber	<	?	super	T	>	actual	;	

Subscription	upstream	;	

QueueSubscription	<	T	>	qs	;	

StripBoundarySubscriber	(	Subscriber	<	?	super	T	>	actual	)	{	
this	.	actual	=	actual	;	
}	

@SuppressWarnings	(	"str"	)	
@Override	
public	void	onSubscribe	(	Subscription	subscription	)	{	
this	.	upstream	=	subscription	;	
if	(	subscription	instanceof	QueueSubscription	)	{	
qs	=	(	QueueSubscription	<	T	>	)	subscription	;	
}	
actual	.	onSubscribe	(	this	)	;	
}	

@Override	
public	void	onNext	(	T	t	)	{	
actual	.	onNext	(	t	)	;	
}	

@Override	
public	void	onError	(	Throwable	throwable	)	{	
actual	.	onError	(	throwable	)	;	
}	

@Override	
public	void	onComplete	(	)	{	
actual	.	onComplete	(	)	;	
}	

@Override	
public	int	requestFusion	(	int	mode	)	{	
QueueSubscription	<	T	>	fs	=	qs	;	
if	(	fs	!	=	null	)	{	
return	fs	.	requestFusion	(	mode	&	~	BOUNDARY	)	;	
}	
return	NONE	;	
}	

@Override	
public	boolean	offer	(	T	value	)	{	
throw	new	UnsupportedOperationException	(	"str"	)	;	
}	

@Override	
public	boolean	offer	(	T	v1	,	T	v2	)	{	
throw	new	UnsupportedOperationException	(	"str"	)	;	
}	

@Override	
public	T	poll	(	)	throws	Exception	{	
return	qs	.	poll	(	)	;	
}	

@Override	
public	void	clear	(	)	{	
qs	.	clear	(	)	;	
}	

@Override	
public	boolean	isEmpty	(	)	{	
return	qs	.	isEmpty	(	)	;	
}	

@Override	
public	void	request	(	long	n	)	{	
upstream	.	request	(	n	)	;	
}	

@Override	
public	void	cancel	(	)	{	
upstream	.	cancel	(	)	;	
}	
}	
}	

public	static	<	T	>	FlowableTransformer	<	T	,	T	>	flowableStripBoundary	(	)	{	
return	new	FlowableStripBoundary	<	T	>	(	null	)	;	
}	

static	final	class	ObservableStripBoundary	<	T	>	extends	Observable	<	T	>	implements	ObservableTransformer	<	T	,	T	>	{	

final	Observable	<	T	>	source	;	

ObservableStripBoundary	(	Observable	<	T	>	source	)	{	
this	.	source	=	source	;	
}	

@Override	
public	Observable	<	T	>	apply	(	Observable	<	T	>	upstream	)	{	
return	new	ObservableStripBoundary	<	T	>	(	upstream	)	;	
}	

@Override	
protected	void	subscribeActual	(	Observer	<	?	super	T	>	s	)	{	
source	.	subscribe	(	new	StripBoundaryObserver	<	T	>	(	s	)	)	;	
}	

static	final	class	StripBoundaryObserver	<	T	>	implements	Observer	<	T	>	,	QueueDisposable	<	T	>	{	

final	Observer	<	?	super	T	>	actual	;	

Disposable	upstream	;	

QueueDisposable	<	T	>	qd	;	

StripBoundaryObserver	(	Observer	<	?	super	T	>	actual	)	{	
this	.	actual	=	actual	;	
}	

@SuppressWarnings	(	"str"	)	
@Override	
public	void	onSubscribe	(	Disposable	d	)	{	
this	.	upstream	=	d	;	
if	(	d	instanceof	QueueDisposable	)	{	
qd	=	(	QueueDisposable	<	T	>	)	d	;	
}	
actual	.	onSubscribe	(	this	)	;	
}	

@Override	
public	void	onNext	(	T	t	)	{	
actual	.	onNext	(	t	)	;	
}	

@Override	
public	void	onError	(	Throwable	throwable	)	{	
actual	.	onError	(	throwable	)	;	
}	

@Override	
public	void	onComplete	(	)	{	
actual	.	onComplete	(	)	;	
}	

@Override	
public	int	requestFusion	(	int	mode	)	{	
QueueDisposable	<	T	>	fs	=	qd	;	
if	(	fs	!	=	null	)	{	
return	fs	.	requestFusion	(	mode	&	~	BOUNDARY	)	;	
}	
return	NONE	;	
}	

@Override	
public	boolean	offer	(	T	value	)	{	
throw	new	UnsupportedOperationException	(	"str"	)	;	
}	

@Override	
public	boolean	offer	(	T	v1	,	T	v2	)	{	
throw	new	UnsupportedOperationException	(	"str"	)	;	
}	

@Override	
public	T	poll	(	)	throws	Exception	{	
return	qd	.	poll	(	)	;	
}	

@Override	
public	void	clear	(	)	{	
qd	.	clear	(	)	;	
}	

@Override	
public	boolean	isEmpty	(	)	{	
return	qd	.	isEmpty	(	)	;	
}	

@Override	
public	void	dispose	(	)	{	
upstream	.	dispose	(	)	;	
}	

@Override	
public	boolean	isDisposed	(	)	{	
return	upstream	.	isDisposed	(	)	;	
}	
}	
}	

public	static	<	T	>	ObservableTransformer	<	T	,	T	>	observableStripBoundary	(	)	{	
return	new	ObservableStripBoundary	<	T	>	(	null	)	;	
}	
}	