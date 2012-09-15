 - Transcription d'une discussion sur [#haskell](http://tunes.org/~nef/logs/haskell/12.09.08)

```
05:58:46 <insitu> hello I am trying to define filter as a catamorphism but without success
05:59:54 --- join: Itkovian (~Itkovian@178-118-76-90.access.telenet.be) joined #haskell
06:00:19 --- join: gusto (~gusto@2001:a60:11ff:1200::42:4) joined #haskell
06:00:20 --- quit: smerz1 (Ping timeout: 252 seconds)
06:02:10 --- quit: dimday (Ping timeout: 252 seconds)
06:02:31 --- join: smerz1 (~my@a177088.upc-a.chello.nl) joined #haskell
06:04:00 <ivanm> insitu: any prticular reason _why_ ?
06:04:02 --- join: merijn (~merijn@inconsistent.nl) joined #haskell
06:04:11 <insitu> fun ?
06:04:38 <insitu> really, I am trying (once more) to understand some stuff from bananas and co.
06:04:51 <timthelion> insitu: :O
06:04:56 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:04:56 --- nick: DrLeQuack -> face
06:04:58 --- join: lyonart77 (~lillomere@host18-116-dynamic.247-95-r.retail.telecomitalia.it) joined #haskell
06:05:07 <lyonart77> ciao a tutti
06:05:15 --- join: mysticc (~satvikc@202.3.77.213) joined #haskell
06:05:19 --- nick: face -> JennyQava
06:05:33 <lyonart77> !list
06:05:36 <lpvb> bananas?
06:05:44 <timthelion> insitu: you are aware that trying to understand reactive bananna may be harmfull to your health.
06:05:55 --- quit: nus- (Read error: Connection reset by peer)
06:05:58 <lpvb> whats wrong with reactive banana
06:05:58 <notdan> > let filter' f lst = foldr (\x acc -> if (f x) then (x:acc) else acc) [] lst
06:05:59 <lambdabot>   not an expression: `let filter' f lst = foldr (\x acc -> if (f x) then (x:a...
06:06:06 <notdan> > filter' f lst = foldr (\x acc -> if (f x) then (x:acc) else acc) [] lst
06:06:07 <lambdabot>   <no location info>: parse error on input `='
06:06:10 <notdan> erh
06:06:17 <timthelion> lpvb: have you seen the examples?
06:06:24 <insitu> yes.
06:06:25 <notdan> > foldr (\x acc -> if (f x) then (x:acc) else acc) [] lst $ odd [1..10]
06:06:26 <lambdabot>   Not in scope: `lst'
06:06:37 <notdan> > \f lst -> foldr (\x acc -> if (f x) then (x:acc) else acc) [] lst $ odd [1..10]
06:06:38 <lambdabot>   Couldn't match expected type `a -> b' against inferred type `[a1]'
06:06:42 <insitu> so i thing, I have seen this: http://hackage.haskell.org/packages/archive/pointless-haskell/0.0.8/doc/html/src/Generics-Pointless-Examples-Examples.html#filterCata
06:06:45 <lpvb> > let filter' f lst = foldr (\x acc -> if (f x) then (x:acc) else acc) [] lst in filter' (<5) [1..10]
06:06:46 <notdan> ok, well, you got the idea, insitu
06:06:47 <lambdabot>   [1,2,3,4]
06:06:57 <notdan> thanks lpvb
06:07:20 <insitu> no, really I dont :-) Because I know that definition. The one I am looking for is the one using directly cata:
06:07:37 <insitu> > newtype Rec f = In { out :: (f (Rec f)) }

06:07:37 <insitu>  
06:07:38 <lambdabot>   <no location info>: parse error on input `newtype'
06:07:40 <lpvb> timthelion: No, but I thought FRP was a good answer to event programming in haskell?
06:08:23 <timthelion> lpvb: they say that.  But then again. no one has ever actually made an FRP application with reacitve bananna...
06:08:29 --- join: alex-shpilkin (~alex@217.10.38.198) joined #haskell
06:08:40 <notdan> hm
06:08:46 <insitu> > cata :: Functor f => (f a -> a) -> (Rec f -> a)
06:08:46 <insitu> cata h = h . fmap (cata h) . out
06:08:46 <insitu>  
06:08:47 <lambdabot>   `f' is not applied to enough type arguments
06:08:47 <lambdabot>  The first argument of `L.Rec' ...
06:09:11 --- quit: dimday (Ping timeout: 246 seconds)
06:09:32 --- quit: lyonart77 ()
06:10:24 <lpvb> > let cata :: Functor f => (f a -> a) -> (Rec f -> a); cata h = h . fmap (cata h) . out; in cata (\Just x -> x)
06:10:26 <lambdabot>   `f' is not applied to enough type arguments
06:10:26 <lambdabot>  The first argument of `L.Rec' ...
06:10:37 --- join: nus (~nus@unaffiliated/nus) joined #haskell
06:10:48 --- join: pchiusano (~pchiusano@c-24-218-205-232.hsd1.ma.comcast.net) joined #haskell
06:10:56 <insitu> do not know how to define Rec f in here...
06:11:24 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:11:24 --- quit: nus (Read error: Connection reset by peer)
06:11:26 --- quit: julmuri (Read error: Connection reset by peer)
06:11:45 --- join: julmuri (~kamazharr@212-226-40-78-nat.elisa-mobile.fi) joined #haskell
06:11:52 <lpvb> what is Rec
06:12:10 <insitu> recursive definition of a functor. Often called Mu I guess.
06:12:10 <lpvb> :i Rec
06:12:19 <lpvb> @src Rec
06:12:19 <lambdabot> newtype Rec a = InR { outR :: Rec a -> a }
06:12:19 <insitu> :i Mu
06:12:32 <insitu> ah great
06:12:47 <insitu> not very familiar with IRC, I fear
06:13:07 <lpvb> bots aren't the IRC protocol
06:13:19 --- join: Apocalisp (~textual@c-174-62-237-65.hsd1.ma.comcast.net) joined #haskell
06:13:29 --- join: jkoshy (~jkoshy@117.204.57.199) joined #haskell
06:13:31 <insitu> I know. I wanted to say I am not very familiar with haskell on IRC
06:13:35 <lpvb> > let cata :: Functor f => (f a -> a) -> (Rec (f a) -> a); cata h = h . fmap (cata h) . out; in cata (\Just x -> x)
06:13:36 <lambdabot>   Couldn't match expected type `L.Rec (f a)'
06:13:36 <lambdabot>         against inferred type `L...
06:13:40 <ivanm> :k Rec
06:13:41 <lambdabot> * -> *
06:13:47 <Jafet> @src Mu
06:13:47 <lambdabot> newtype Mu f = In { out :: f (Mu f) }
06:13:53 <ivanm> lpvb: :t and :k work, but :i doesn't
06:13:53 <insitu> which obviously includes understanding lambdabot
06:14:00 --- join: nus (~nus@unaffiliated/nus) joined #haskell
06:14:14 --- quit: osa1 (Quit: Konversation terminated!)
06:14:35 --- join: jasonkuhrt (~jasonkuhr@74.198.87.124) joined #haskell
06:15:03 <insitu> So given data L a l = Nil | L a l deriving Show
06:15:04 <insitu>  
06:15:13 <insitu> One can define type List a = Rec (L a)
06:15:50 --- quit: dimday (Ping timeout: 246 seconds)
06:17:43 --- quit: janinge (Quit: janinge)
06:17:49 <Franciman> hey people, where can I find swierstra duponchel paper about their arrow parser?
06:18:11 <ivanm> Franciman: google? :p
06:18:14 --- quit: ousado (Ping timeout: 265 seconds)
06:18:22 <Franciman> I have already tried but can't seem to find :(
06:18:57 <Franciman> ivanm, do you know its title?
06:19:07 <Franciman> maybe using the title I'll get more results
06:19:08 <ivanm> no idea which paper you're referring to :)
06:19:10 <merijn> Franciman: Did you use Google or Google Scholar?
06:19:14 <Franciman> google
06:19:29 <merijn> Franciman: You are likely to have more luck on Google Scholar
06:19:38 <merijn> Or, alternatively, citeseerx
06:19:45 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:20:23 --- join: mikecb (~quassel@108.28.251.107) joined #haskell
06:20:42 <Franciman> great I found it, thanks so muhc
06:20:44 <Franciman> *much
06:20:54 --- join: choo (~choo@14.145.137.28) joined #haskell
06:22:00 --- quit: heruur (Quit: Leaving.)
06:22:03 --- join: heruur1 (~tvh@223-148-103-86.dynamic.dsl.tng.de) joined #haskell
06:22:09 --- join: janinge (~janinge@51.80-202-237.nextgentel.com) joined #haskell
06:22:52 <insitu>  > let cata :: Functor f => (f a -> a) -> (Mu f -> a); cata h = h
06:22:52 <insitu>        . fmap (cata h) . out; in cata (\Just x -> x)
06:23:01 <insitu> :t cata
06:23:02 <lambdabot> forall (f :: * -> *) a. (Functor f) => (f a -> a) -> Mu f -> a
06:24:04 <Jafet> :t cata fromJust
06:24:05 <lambdabot> forall a. Mu Maybe -> a
06:24:46 --- join: phischu (~pschuster@p4FD31A35.dip0.t-ipconnect.de) joined #haskell
06:24:48 --- quit: dimday (Ping timeout: 268 seconds)
06:24:49 <Jafet> This doesn't sound like a very interesting function
06:24:57 --- join: anders_ (~quassel@h146n2-vrr-d2.ias.bredband.telia.com) joined #haskell
06:25:14 <insitu> hmm, it is a generalized fold actually
06:25:26 --- quit: choo (Ping timeout: 268 seconds)
06:25:26 --- join: johntromp (~johntromp@ool-4575e8f7.dyn.optonline.net) joined #haskell
06:26:01 --- quit: anders_ (Client Quit)
06:26:11 <dolio> I think he means cata fromJust.
06:26:14 <dolio> And it isn't.
06:26:24 <insitu> really not.
06:26:25 <ivanm> dolio: interesting or a generalised fold? ;)
06:27:07 <dolio> It's 'f Nothing = error "fromJust" ; f (Just x) = fromJust (f x)'
06:27:10 <dolio> Isn't interesting.
06:27:51 <dolio> Oh, wait, it's 'f (Just x) = f x'
06:28:10 <ivanm> so.... it's only useful for Just (Just (Just (Just ... ) ) ) ?
06:28:18 <dolio> No, it's not useful there, either.
06:28:18 <ivanm> s/useful/non-error-causing/
06:28:28 --- quit: synonymous (Quit: Leaving.)
06:28:31 --- quit: jianmeng (Ping timeout: 268 seconds)
06:28:35 <dolio> Unless you think spinning forever is more useful than throwing an exception.
06:28:41 --- quit: jfredett (Ping timeout: 246 seconds)
06:28:41 <ivanm> yeah, I realised "useful" wasn't the term as soon as I hit enter :p
06:28:55 <Jafet> I stopped thinking after Mu Maybe -> a
06:28:58 --- join: osa1 (~sinan@212.175.134.197) joined #haskell
06:29:08 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:29:33 --- join: phischu1 (~pschuster@p4FD3086C.dip0.t-ipconnect.de) joined #haskell
06:29:43 --- quit: kcj (Ping timeout: 256 seconds)
06:29:44 <insitu> but it is useful for recursive datatypes, which maybe is not
06:29:44 --- join: obk (86bfe845@gateway/web/freenode/ip.134.191.232.69) joined #haskell
06:30:33 <insitu> well, useful as in "makes your head ache when you try to think too hard about it but some people write thesis about it"
06:30:45 <Jafet> Origami nuts...
06:30:45 <ivanm> and edwardk loves it? :p
06:31:14 --- quit: phischu (Ping timeout: 240 seconds)
06:31:20 <byorgey> insitu: no one is denying that cata is useful.
06:31:26 <byorgey> it's cata fromJust  which isn't useful.
06:31:41 --- join: emmanuelux (~emmanuelu@2a01:e35:2e4d:9010:dc25:40be:b1c8:e136) joined #haskell
06:31:45 --- join: anders_ (~quassel@h146n2-vrr-d2.ias.bredband.telia.com) joined #haskell
06:32:00 <Jafet> If you ask me, I think cata isn't very useful either
06:32:00 --- nick: anders_ -> Craps
06:32:08 <insitu> :-)
06:32:10 <Jafet> I've never used it, at least
06:32:11 <ivanm> @type cata head
06:32:12 <lambdabot> forall a. Mu [] -> a
06:32:22 <ivanm> just as useless?
06:32:44 <insitu> :t head
06:32:45 <lambdabot> forall a. [a] -> a
06:33:12 <dolio> cata head is 'walk to the left-most leaf of the tree, and then throw an error.'
06:33:12 <byorgey> any function of type  forall a. (something that does not involve a) -> a   probably won't be winning any awards for usefulness, let's put it that way
06:33:15 <Jafet> Mu k is pretty useless for most k
06:33:27 <Jafet> You probably want Mu (k a)
06:33:33 <ivanm> Jafet: for which k _is_ it useful for?
06:33:38 <byorgey> Jafet: what?
06:33:56 <dolio> Mu Maybe is natural numbers.
06:33:57 <byorgey> there's nothing wrong with  Mu Maybe   and   Mu [] .
06:34:06 --- quit: dimday (Ping timeout: 272 seconds)
06:34:18 <byorgey> Mu []  is  rose tree shapes.
06:34:35 --- quit: Apocalisp (Quit: Computer has gone to sleep.)
06:34:39 --- join: strg (~strg@a89-182-11-35.net-htp.de) joined #haskell
06:34:46 <Jafet> Exactly what I said! Pretty but useless.
06:35:01 <ivanm> Jafet: not _precisely_ what you said...
06:35:11 <byorgey> natural numbers are useless?
06:35:37 <ivanm> byorgey: how do you use Mu Maybe as naturals?
06:36:04 * Craps waves to everybody
06:36:15 <ivanm> since when did we have card games here?
06:36:17 <Jafet> Tediously
06:36:18 <ivanm> ;)
06:36:24 <byorgey> > let toInt = cata (maybe 0 succ) in toInt (Mu (Just (Mu (Just (Mu (Just (Mu Nothing)))))))
06:36:25 <lambdabot>   Not in scope: data constructor `Mu'Not in scope: data constructor `Mu'Not i...
06:36:34 <byorgey> oh, it's In
06:36:53 <byorgey> > let toInt = cata (maybe 0 succ) in toInt (In (Just (In (Just (In (Just (In Nothing)))))))
06:36:55 <merijn> :t cata
06:36:55 <lambdabot>   3
06:36:56 <lambdabot> forall (f :: * -> *) a. (Functor f) => (f a -> a) -> Mu f -> a
06:36:58 --- quit: replore_ (Remote host closed the connection)
06:37:06 <ivanm> huh
06:37:27 --- join: ParahSailin_ (~parah@adsl-69-151-200-58.dsl.hstntx.swbell.net) joined #haskell
06:37:37 <byorgey> ivanm: at each level you have either Nothing (representing zero) or Just wrapped around another Mu Maybe (representing successor)
06:37:44 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:37:48 <ivanm> yup, got it now
06:39:09 --- join: Apocalisp (~textual@c-174-62-237-65.hsd1.ma.comcast.net) joined #haskell
06:39:31 <Jafet> data Fix f = Fix (f (Fix f))
06:39:48 <int-e> Jafet: as one data point regarding whether this x32 ABI helps in practice, https://sites.google.com/site/x32abi/ has a comparison for two SPEC benchmarks (181.mcf and 186.mcf) that is consistent with the "best of two worlds" goal. The paper reports a 13.4% average speedup on the SPEC2000 C benchmarks compared to 64 bit mode. They have no number for the 32-bit compatibility mode, but the diagrams indicate that they usually...
06:39:54 <int-e> ...beat that mode, too, and significantly if the program makes use of 64 bit integers.
06:40:42 --- quit: pchiusano (Quit: Textual IRC Client: http://www.textualapp.com/)
06:41:02 --- quit: smerz1 (Ping timeout: 244 seconds)
06:41:02 <int-e> (Or perhaps I missed the number.)
06:41:07 --- join: jianmeng (~mengjian@123.138.150.42) joined #haskell
06:41:19 --- join: nimor (~jkr@129.48.202.84.customer.cdi.no) joined #haskell
06:41:31 --- join: jfredett (~jfredett@pool-108-20-213-251.bstnma.fios.verizon.net) joined #haskell
06:42:20 <Jafet> int-e: interesting
06:42:20 --- quit: dimday (Ping timeout: 272 seconds)
06:42:36 <Jafet> Depending on how contrived the benchmark is, the difference could probably be higher
06:42:39 --- join: casion (~casion@pool-71-100-13-87.tampfl.fios.verizon.net) joined #haskell
06:42:52 --- quit: F1skr (Quit: WeeChat 0.3.8)
06:42:52 --- quit: ParahSailin_ (Read error: Connection reset by peer)
06:45:06 --- quit: Ptival (Read error: Connection reset by peer)
06:46:34 --- join: dimday (~amcvega@114.108.252.168) joined #haskell
06:46:59 --- join: twanvl_ (~Twan@g106053.upc-g.chello.nl) joined #haskell
06:47:04 --- join: anRch (~markmilli@ip-64-134-243-164.public.wayport.net) joined #haskell
06:48:58 --- quit: aranea (Quit: leaving)
06:50:47 --- join: Ptival (~ptival@82.240.39.82) joined #haskell
06:50:50 --- quit: dimday (Ping timeout: 246 seconds)
06:51:05 --- quit: twanvl (Ping timeout: 246 seconds)
06:51:16 --- quit: janinge (Quit: janinge)
06:51:51 --- quit: Khisanth (Ping timeout: 272 seconds)
06:52:54 --- quit: Apocalisp (Quit: Computer has gone to sleep.)
06:53:52 --- join: choo (~choo@113.68.61.117) joined #haskell
06:54:31 --- quit: Itkovian (Remote host closed the connection)
06:54:44 --- join: Itkovian (~Itkovian@vpnh120.ugent.be) joined #haskell
06:54:51 <Z`> hey people, when I write: newtype State s a = (s -> (a,s)) why :t State is (s -> (a, s)) -> State s a, and not State s a -> (s -> (a,s)) ?
06:55:01 --- join: niklasb (~codeslay0@p5B310C4D.dip0.t-ipconnect.de) joined #haskell
06:55:10 <Z`> I missed the constructor State (s -> ...)
06:55:13 <ivanm> Z`: because that's the type of the State constructor
06:55:19 <ivanm> the type State doesn't have a type
06:55:26 <ivanm> but it does have kind * -> * -> *
06:55:27 <Z`> a ha!
06:55:36 <Z`> so :t State returns the type of the constructor
06:55:41 --- join: ericmj (~ericmj@h245n6-g-ml-a11.ias.bredband.telia.com) joined #haskell
06:55:45 <Z`> thanks ivan
06:55:48 <Z`> ivanm: *
06:56:05 <ivanm> if you _call_ the constructor the same as the type, yes :)
06:57:02 --- quit: choo (Read error: Connection reset by peer)
06:58:10 <Z`> so the constructor accepts functions that map s to (a,s) and returns a State type ?
06:58:21 --- quit: ekponk (Remote host closed the connection)
06:58:21 <ivanm> yes
06:58:55 <rwbarton> it is a data constructor for the type State s a so naturally it returns a value of type State s a :P
06:59:05 --- join: choo (~choo@183.5.236.107) joined #haskell
06:59:09 <rwbarton> or "constructs" a value...
06:59:28 --- quit: kmels (Ping timeout: 276 seconds)
07:00:12 --- join: ParahSailin_ (~parah@adsl-69-151-200-58.dsl.hstntx.swbell.net) joined #haskell
07:02:03 --- quit: nomeata (Ping timeout: 245 seconds)
07:02:14 --- join: l0p3n (~squig@unaffiliated/l0p3n) joined #haskell
07:02:20 --- join: schlaftier (~daniel@HSI-KBW-078-042-201-118.hsi3.kabel-badenwuerttemberg.de) joined #haskell
07:03:36 --- quit: choo (Read error: No route to host)
07:04:29 --- quit: eikke (Ping timeout: 240 seconds)
07:04:47 --- quit: sanemat (Remote host closed the connection)
07:05:20 --- join: sanemat (~sanemat@122x216x10x66.ap122.ftth.ucom.ne.jp) joined #haskell
07:05:33 --- join: anonus (anonymous@2a01:7e00::f03c:91ff:fedf:2cc7) joined #haskell
07:05:37 --- join: k0k01 (~kkostya@adsl-98-85-4-123.mco.bellsouth.net) joined #haskell
07:08:07 --- join: dcracked (~dcracked@p549BABC4.dip.t-dialin.net) joined #haskell
07:08:58 <applicative> @type Left
07:08:59 <lambdabot> forall a b. a -> Either a b
07:10:02 --- join: scooty-puff (~andy@184-97-231-51.mpls.qwest.net) joined #haskell
07:11:36 --- join: choo (~choo@113.68.60.229) joined #haskell
07:12:01 --- quit: dimituri (Remote host closed the connection)
07:12:36 --- join: ttt-- (~ubuntu@94-224-175-203.access.telenet.be) joined #haskell
07:13:59 --- join: dimituri (~dimituri@balticom-142-93-64.balticom.lv) joined #haskell
07:14:40 --- quit: sanemat (Remote host closed the connection)
07:15:42 --- join: shapr (~shapr@c-71-207-252-122.hsd1.al.comcast.net) joined #haskell
07:15:56 --- join: dgpratt (~dpratt71@pool-72-65-106-216.ptldme.east.myfairpoint.net) joined #haskell
07:16:51 --- join: hajimehoshi (~hajimehos@PPPa652.otemachi.acca.dti.ne.jp) joined #haskell
07:18:13 --- quit: DasIch (Quit: DasIch)
07:18:43 --- quit: dcracked (Remote host closed the connection)
07:19:22 --- join: Khisanth (~Khisanth@50.14.244.111) joined #haskell
07:19:33 --- quit: hiptobecubic (Ping timeout: 245 seconds)
07:19:45 --- quit: inigo_ (Ping timeout: 260 seconds)
07:20:18 --- quit: Soft (Ping timeout: 244 seconds)
07:20:22 --- join: smerz1 (~my@a177088.upc-a.chello.nl) joined #haskell
07:20:23 * hackagebot haskell-src-meta 0.6 - Parse source to template-haskell abstract syntax.  http://hackage.haskell.org/package/haskell-src-meta-0.6 (BenMillwood)
07:20:55 --- quit: dschoepe (Ping timeout: 276 seconds)
07:21:26 --- join: Borgvall (~iceweasel@p5DC15578.dip.t-dialin.net) joined #haskell
07:23:31 --- join: drchaos (~drchaos@cpe-173-175-125-28.satx.res.rr.com) joined #haskell
07:23:39 <luite> benmachine: thanks :)
07:24:02 --- join: mun (~anonymous@94-193-244-76.zone7.bethere.co.uk) joined #haskell
07:24:02 --- quit: mun (Changing host)
07:24:02 --- join: mun (~anonymous@unaffiliated/mun) joined #haskell
07:24:12 <benmachine> luite: oh, were you a person who wanted that?
07:24:15 <benmachine> I forget these things :P
07:24:54 <benmachine> luite: oh wait, probably going to do an 0.6.0.1
07:25:14 * benmachine hasn't put generating haddock into his testing sequence yet
07:25:18 --- join: dcracked (~dcracked@84.155.171.196) joined #haskell
07:26:46 --- quit: ivanm (Read error: Connection timed out)
07:27:00 <luite> benmachine: yeah i use jmacro in ghcjs, which i ported to 7.6.1 :) jmacro uses haskell-src-meta
07:27:15 <benmachine> oh, fair enough
07:27:20 --- quit: tranma (Remote host closed the connection)
07:27:34 --- join: ivanm (~ivan@115.187.254.102) joined #haskell
07:27:57 --- join: estebistec (~estebiste@72.133.228.205) joined #haskell
07:27:58 --- join: owst (~owst@188-222-49-76.zone13.bethere.co.uk) joined #haskell
07:28:17 --- join: tranma (~tranma@123-243-95-236.static.tpgi.com.au) joined #haskell
07:28:21 --- join: dimmy1 (~Adium@p5090AF17.dip.t-dialin.net) joined #haskell
07:30:02 --- quit: juhp (Ping timeout: 255 seconds)
07:31:06 --- quit: dimmy (Ping timeout: 272 seconds)
07:33:00 --- quit: nus (Read error: Connection reset by peer)
07:33:04 --- quit: k0k01 (Quit: Leaving)
07:33:18 --- join: Paprikachu (~chatzilla@178.115.250.176.wireless.dyn.drei.com) joined #haskell
07:33:20 --- quit: kennyd (Read error: Connection reset by peer)
07:33:33 --- join: nus (~nus@unaffiliated/nus) joined #haskell
07:33:54 --- join: stephenjudkins (~stephen@c-24-22-61-51.hsd1.or.comcast.net) joined #haskell
07:34:34 --- quit: AfC (Quit: Leaving.)
07:34:35 <benmachine> hmph, haddock doesn't store its interface files in versioned directories
07:34:42 <benmachine> so I can't test multiple versions of haddock
07:35:33 --- join: kennyd (~kennyd@93-141-88-88.adsl.net.t-com.hr) joined #haskell
07:36:11 <insitu> @byorgey so maybe you can help me defining filter with cata ?
07:36:12 <lambdabot> Unknown command, try @list
07:36:25 <insitu> @list
07:36:25 <lambdabot> http://code.haskell.org/lambdabot/COMMANDS
07:36:42 <benmachine> insitu: because of lambdabot we tend not to use @ to address people in here
07:36:51 --- join: stat_vi (~stat@dslb-094-218-224-050.pools.arcor-ip.net) joined #haskell
07:37:00 --- quit: nus (Read error: Connection reset by peer)
07:37:09 <insitu> sorry,
07:37:52 <otters> I don't even think @nick will ping most people
07:38:11 --- join: geisthaus (~quassel@pool-71-182-109-53.syrcny.east.verizon.net) joined #haskell
07:38:31 * benmachine nudges hackagebot 
07:38:34 --- join: nus (~nus@unaffiliated/nus) joined #haskell
07:38:47 --- nick: Franciman -> Franciman_soccer
07:39:24 --- join: Soft (~soft@unaffiliated/soft) joined #haskell
07:39:44 --- quit: tobias2 (Ping timeout: 246 seconds)
07:40:23 * hackagebot haskell-src-meta 0.6.0.1 - Parse source to template-haskell abstract syntax.  http://hackage.haskell.org/package/haskell-src-meta-0.6.0.1 (BenMillwood)
07:40:47 <insitu> spoiled by twitter, I guess
07:40:57 --- join: edwardk (~edwardk@pdpc/supporter/professional/edwardk) joined #haskell
07:41:38 <insitu> I am still stuck with my filter
07:41:50 --- quit: jfredett (Ping timeout: 246 seconds)
07:42:41 --- join: gmci (~gmci@destroy.all.humans.vntx.net) joined #haskell
07:43:21 --- quit: tranma (Remote host closed the connection)
07:43:43 --- quit: iwant2beawookie (Remote host closed the connection)
07:44:08 --- quit: anRch (Quit: anRch)
07:44:31 <benmachine> insitu: what do you mean by cata exactly?
07:45:23 <insitu> > cata :: Functor f => (f a -> a) -> (Mu f -> a)
07:45:23 <insitu> cata h = h . fmap (cata h) . out
07:45:23 <insitu>  
07:45:25 <lambdabot>   Overlapping instances for GHC.Show.Show ((f a -> a) -> L.Mu f -> a)
07:45:25 <lambdabot>    aris...
07:45:35 <benmachine> oh right
07:45:47 --- join: tibbe (~tibbe@h158n3-vrr-d2.ias.bredband.telia.com) joined #haskell
07:45:48 <insitu> I was trying to define filter using cata
07:46:03 <benmachine> as in, list filter?
07:46:06 <insitu> yes
07:46:14 <benmachine> ok so can you define lists using Mu?
07:46:19 <insitu> yes
07:46:27 <benmachine> what f do you use?
07:46:33 <insitu> learning.
07:46:43 <insitu> understanding some papers I read.
07:46:46 <benmachine> err
07:46:47 <insitu> fun.
07:46:56 <benmachine> I think you may have misunderstood my question
07:47:01 <benmachine> I have no idea what question you're answering :P
07:47:07 --- join: tranma (~tranma@123-243-95-236.static.tpgi.com.au) joined #haskell
07:47:10 --- join: sambio (~sam@190.57.227.107) joined #haskell
07:47:11 <insitu> ah! sorry,
07:47:19 --- quit: dimituri (Remote host closed the connection)
07:47:26 <insitu> yes, I see my mistake
07:47:34 * obk pings edwardk
07:47:42 --- quit: Borgvall (Remote host closed the connection)
07:47:42 * edwardk is pinged.
07:47:44 <obk> edwardk: I have a mystery on my hands: https://github.com/ekmett/lens/issues/36
07:48:00 <obk> shachaf just tested it and on his machine, it works fine.
07:48:01 <insitu> you mean the f in Functor f
07:48:11 <rwbarton> Has anyone here used LENSES??
07:48:13 --- join: ts33kr (~ts33kr@93.178.244.116) joined #haskell
07:48:17 --- join: roasbeef (~olaoluwao@cab19-178.1scom.net) joined #haskell
07:48:18 <benmachine> insitu: I mean, when you are defining lists with Mu, you use some functor
07:48:19 <obk> We are buffled by why :i At doesn't show any instances.
07:48:22 <rwbarton> I have a slightly complicated task and I'm wondering whether there is a lens library that is a good fit for it
07:48:25 <benmachine> I want to know which functor you're using
07:48:35 <edwardk> ghci> fromList [("hello",12)] ^. at "hello"
07:48:36 <edwardk> Just 12
07:48:42 <insitu> data L a l = Nil | L a l deriving Show ; type List a = Mu (L a)
07:48:50 <obk> edwardk: Exactly.
07:49:01 <edwardk> obk: that was me running it. hrmm
07:49:03 <benmachine> insitu: ok, cool
07:49:13 <edwardk> rwbarton: i'm fond of 'lens' personally =P
07:49:15 --- quit: johntromp (Remote host closed the connection)
07:49:32 <edwardk> obk: do you have multiple versions of containers installed?
07:49:32 <rwbarton> Basically I have a JSON object with one field that is a list of "widgets", I have an IO function that "improves" a widget, and I want to improve all the widgets in the object and write out the rest of the object unmodified
07:49:46 --- quit: smerz1 (Ping timeout: 248 seconds)
07:49:47 <rwbarton> are lenses at all appropriate here?
07:49:58 --- quit: ablokzijl (Ping timeout: 252 seconds)
07:50:10 --- quit: schovi (Remote host closed the connection)
07:50:14 <insitu> benmachine I do not see how I can define a sensible (f a -> a) function that I could pass
07:50:16 <obk> edwardk: Yes, I tried installing containers-0.5.0.0 which failed miserably.
07:50:22 <insitu> to cata for defining filter
07:50:30 <obk> I am using 0.4.2.1
07:50:30 <benmachine> insitu: yeah, honestly I don't know the answer either, but I reckon it can be done
07:50:32 <edwardk> rwbarton: probably. you can make a lens that can visit the field, and a traversal that can walk the list (with traverse), compose them, and then use it with the io action
07:50:47 <rwbarton> @hackage lens
07:50:47 <lambdabot> http://hackage.haskell.org/package/lens
07:50:48 --- quit: hallski (Ping timeout: 245 seconds)
07:50:54 <insitu> benmachine I am pretty sure it can be done, just trying to reconstruct it
07:51:03 <rwbarton> ok, I will let you know if I get stuck!
07:51:09 <edwardk> e.g. (yourlens.traverse) (yourioaction) yourjsonobject
07:51:10 --- join: smerz1 (~my@a177088.upc-a.chello.nl) joined #haskell
07:51:10 <benmachine> insitu: ok so we get cata :: (L a l -> l) -> (Mu (L a) -> l), right?
07:51:27 --- join: johntromp (~johntromp@ool-4575e8f7.dyn.optonline.net) joined #haskell
07:51:28 --- quit: k00mi (Ping timeout: 276 seconds)
07:51:31 --- quit: doomMonkey (Quit: Leaving)
07:51:33 <rwbarton> traverse is a lens function or from Traversable?
07:51:34 <insitu> benmachine found this definition http://hackage.haskell.org/packages/archive/pointless-haskell/0.0.8/doc/html/src/Generics-Pointless-Examples-Examples.html#filterCata but it does not help much because it uses some more cryptic operators
07:51:35 <edwardk> obk: when you use ghc-pkg list | grep containers what shows up?
07:51:43 <edwardk> rwbarton: Data.Traversable.traverse is a legal Traversal
07:51:49 <benmachine> insitu: and we know what we want l to be, because we want to get a list out
07:51:49 <rwbarton> ah okay
07:51:50 <edwardk> thats in base =)
07:51:58 --- quit: owst (Read error: Operation timed out)
07:52:02 --- quit: jeltsch (Quit: jeltsch)
07:52:17 <benmachine> insitu: yeah, ignore that
07:52:26 <edwardk> i should probably write some json lenses/traversals
07:53:06 <edwardk> obk: i'm wondering if at ghci your problem is you are getting different versions of the packae and so the instances for one aren't showing up when using the other
07:53:14 <obk> edwardk: I unregistered containers 0.5.0.0 and now it works.
07:53:14 --- join: rking (~rking@unaffiliated/rking) joined #haskell
07:53:21 <insitu> benmachine exactly. I can define filter' to be like this:
07:53:24 <edwardk> then that was it =)
07:53:30 <insitu> filtr :: (a -> Bool) -> L a l -> L a l
07:53:30 <insitu> filtr p Nil     = Nil
07:53:30 <insitu> filtr p (L a l) | p a = L a l
07:53:30 <insitu>                 | otherwise = Nil
07:53:33 <insitu>  
07:53:53 <edwardk> rwbarton: what json lib are you using
07:54:11 <insitu> benmachine cata (filter' p) does not typecheck
07:54:16 <obk> edwardk: I am eagerly waiting the day I can use containers-0.5.0.0, probably with ghc-7.6, and presumably whatever version of lens will be current that happy day.
07:54:29 <benmachine> insitu: indeed it does not, so don't do that then :P
07:54:30 --- join: iwant2beawookie (~rr@gateway/tor-sasl/oracle) joined #haskell
07:54:36 --- quit: till_t (Quit: Leaving...)
07:54:36 <rwbarton> aeson
07:54:37 --- join: tobias2 (~tobias@dslb-084-060-004-028.pools.arcor-ip.net) joined #haskell
07:55:23 --- quit: gienah (Quit: leaving)
07:55:26 <edwardk> obk: what version of lens are you on?
07:55:36 <benmachine> insitu: ok so our ultimate goal is filterCata :: (a -> Bool) -> Mu (L a) -> Mu (L a), right?
07:55:57 <benmachine> where filterCata p = cata (helper p), and we need to define helper
07:56:24 --- join: doomMonkey (michel@5112-1-367296-01.services.oktawave.com) joined #haskell
07:56:29 <edwardk> funny, we pushed up an answer at the same instant =)
07:56:46 --- join: CoverSlide (~richard@pool-71-103-141-185.lsanca.fios.verizon.net) joined #haskell
07:56:57 <insitu> benmachine: yes.
07:57:12 <benmachine> insitu: ok, so first, what's the type of helper?
07:57:37 <obk> edwardk: Actually I am using data-lens, but I just installed lens-2.6.1 in the hope of migrating to it. I'm running some tests and creating an HUnit test file that demonstrates using "common" scenarios - my developers aren't advanced, and anyway reverse-engineering the "recipes" from the documentation isn't always trivial ;-)
07:57:41 <benmachine> insitu: I mean, there's only one thing it can be, if we set it up like this
07:57:48 <edwardk> rwbarton: hrmm, going to take a look at aeson and see if i can put together a few traversals/lenses
07:57:49 <insitu> :t cata
07:57:50 <lambdabot> forall (f :: * -> *) a. (Functor f) => (f a -> a) -> Mu f -> a
07:58:00 <rwbarton> great, thanks
07:58:05 <edwardk> obk: let me know if you get stuck. i'm happy to help
07:58:18 <obk> edwardk: Will do. Thanks!
07:58:27 <latro`a> @src cata
07:58:27 <lambdabot> Source not found. Do you think like you type?
07:58:29 <latro`a> :|
07:58:38 --- join: porco (~porco@125.33.81.91) joined #haskell
07:59:30 <insitu> benmachine: this implies that helper :: (a -> bool) -> (L a -> Mu (L a))
07:59:47 <hpc> cata f (Mu xs) = f (fmap (cata f) xs)
07:59:54 <hpc> :k Mu
07:59:55 <lambdabot> (* -> *) -> *
08:00:01 <obk> edwardk: One thing I am missing is something I called ".:" (not sure what the right thing would be). It basically means "flip", allowing me to write: record .: foo .~ 4 instead of foo .~4 $ record. Am I missing something in lens, or do I need to define it myself?
08:00:02 <hpc> :t let cata f (Mu xs) = f (fmap (cata f) xs) in cata
08:00:03 <lambdabot> Not in scope: data constructor `Mu'
08:00:08 <benmachine> insitu: 'L a' isn't a type
08:00:10 <hpc> :t let cata f (In xs) = f (fmap (cata f) xs) in cata
08:00:11 <lambdabot> forall (t :: * -> *) b. (Functor t) => (t b -> b) -> Mu t -> b
08:00:15 <hpc> ha!
08:00:16 <benmachine> insitu: L has two type parameters, remember
08:00:23 <insitu> benmachine: right
08:00:24 * hackagebot hscolour 1.20.3 - Colourise Haskell code.  http://hackage.haskell.org/package/hscolour-1.20.3 (MalcolmWallace)
08:00:25 <edwardk> obk: that one i don't provide
08:00:26 <obk> edwardk: Another thing I am missing are ++~ and ++= :-)
08:00:39 <edwardk> obk: Data.Monoid.Lens.<>~
08:00:41 <hpc> :t cata join
08:00:42 <lambdabot> forall (f :: * -> *) a. (Monad f, Functor f) => Mu f -> f a
08:00:49 <hpc> ooh
08:00:58 <insitu> benmachine: should it be helper :: (a -> bool) -> (L a l -> Mu (L a))
08:01:03 --- quit: scooty-puff (Quit: scooty-puff)
08:01:11 <benmachine> insitu: no
08:01:26 <benmachine> insitu: think of what the 'a' variable is in the cata call
08:01:28 <insitu> benmachine: no
08:01:30 <latro`a> having trouble doing the type inference in my head for cata -_-
08:01:31 --- join: xwl (~user@182.48.111.224) joined #haskell
08:01:31 --- quit: arbn (Read error: Connection reset by peer)
08:01:35 <edwardk> tryng to think if there is a good place i could put your $$ operator or whatever it should be called
08:01:41 <benmachine> latro`a: I'm not doing it in my head :P
08:01:54 <edwardk> .: are a couple of pretty valuable symbols to lose and they don't fit the semiotics of lens
08:01:58 --- join: Apocalisp (~textual@c-174-62-237-65.hsd1.ma.comcast.net) joined #haskell
08:01:58 <latro`a> I know, but if I can't do it in my head I have some trouble seeing why it should be what it is
08:02:06 <latro`a> in particular why the first argument is t b -> b
08:02:09 --- quit: tibbe (Quit: tibbe)
08:02:33 <edwardk> obk: can you put in a feature request for something like .: with an example type signature on the issues page?
08:02:43 <benmachine> latro`a: it's a bit weird
08:02:47 <latro`a> or I guess rather why fmap (cata f) xs is a t b
08:02:48 <obk> edwardk: Sure.
08:02:57 <insitu> benmachine: helper p should be (a -> bool) -> (f a -> a) for some f
08:03:00 <latro`a> because doesn't that make xs a t (t b)
08:03:01 --- join: tensorpudding (~michael@108.87.18.210) joined #haskell
08:03:05 <latro`a> or is that the whole point
08:03:17 <obk> edwark: Let me tinker with it a bit (precedence and so on) and submit a concrete proposal.
08:03:20 <benmachine> insitu: hmm, I don't think so
08:03:24 <insitu> benmachine: then a should be Mu (L a)
08:03:36 <benmachine> insitu: I think you're getting your a's confused here :P
08:03:37 <benmachine> or I am
08:03:51 <edwardk> sure, you may want to include the precedence motivations as well. i've got a good guess at a precedence for it
08:03:59 <edwardk> it'd be nice to see we converged =)
08:04:01 <insitu> benmachine: but type of cata if clear
08:04:04 <insitu> :t cata
08:04:05 <lambdabot> forall (f :: * -> *) a. (Functor f) => (f a -> a) -> Mu f -> a
08:04:05 <benmachine> insitu: ok let's stick to the type of list elements being a, and say cata :: (f b -> b) -> (Mu f -> b)
08:04:13 <benmachine> insitu: or vice versa, I don't mind
08:04:14 <obk> edwardk: BTW, why <>~ and not ++~ ? Is it because ++ is only defined for lists? Arguably ++ should mean mappend...
08:04:18 --- quit: ivanm (Read error: Connection timed out)
08:04:19 --- quit: mikecb (Ping timeout: 246 seconds)
08:04:21 --- join: c_wraith (~c_wraith@commie.pwrsrc.net) joined #haskell
08:04:26 <edwardk> (<>) is in Data.Monoid as an alias for mappend.
08:04:47 <obk> edwardk: I guess so. One more thing for the cheat sheet for my developers.
08:04:51 <hpc> is Mu anywhere on hackage?
08:05:05 <latro`a> istr it's like Data.Fix or something
08:05:12 --- join: ivanm (~ivan@115.187.254.102) joined #haskell
08:05:16 <latro`a> but I'm probably wrong
08:05:28 <edwardk> i could probably be talked into adding ++~, <++~, ++=, and <++= to Data.List.Lens
08:05:35 <edwardk> especially if there was a patch that added them ;)
08:06:00 <edwardk> but they should be the actual list (++) rather than the monoidal version in Data.Monoid.Lens
08:06:06 --- join: DasIch (~DasIch@p57A4E460.dip.t-dialin.net) joined #haskell
08:06:16 --- quit: rprije ()
08:06:27 <edwardk> that way if people wanted to use them for instance selection, etc. it would work
08:07:06 --- join: arbn (~arbn@71-87-150-49.static.hlrg.nc.charter.com) joined #haskell
08:07:21 <insitu> benmachine: I think I have some glimpse on what's going on
08:07:40 --- quit: ttt-- (Ping timeout: 264 seconds)
08:07:59 <benmachine> insitu: I think I have the solution
08:08:44 <benmachine> insitu: also I realised we don't actually need to pass p to helper
08:08:57 <benmachine> insitu: we can have filterCata p = cata helper where helper = ...
08:09:00 <insitu> benmachine: type of helper p should be L a (Rec (L a)) -> Rec (L a)
08:09:21 --- join: peteretep (~sheriff@li238-146.members.linode.com) joined #haskell
08:09:25 <benmachine> insitu: that sounds about right (assuming Rec = Mu)
08:09:33 --- join: gongyiliao (~gongyilia@d56h215.public.uconn.edu) joined #haskell
08:09:43 <insitu> oh, sorry. I used Rec because that's what is used in the paper I am reading
08:09:53 <hpaste> peteretep pasted â€œErrorâ€ at http://hpaste.org/74440
08:09:55 <benmachine> insitu: 'sfine, I can cope :P
08:10:05 <peteretep> Hi, I'd like some help understanding the above
08:10:13 <peteretep> I'd have thought that my type class declarations for the data type
08:10:14 * benmachine runs :%s/Mu/Rec/g
08:10:23 <insitu> so helper :: (a -> Bool) -> L a (Mu (L a)) -> Mu (L a)
08:10:24 <peteretep> Insist that x can do Eq and Show
08:10:36 <peteretep> But it seems that I also need to declare those in my function definitinos
08:10:45 <benmachine> peteretep: datatype contexts don't do what you want them to do
08:10:56 --- quit: edwardk (Ping timeout: 244 seconds)
08:10:59 <peteretep> benmachine: OK. Is there any way to get this behavior?
08:10:59 <benmachine> peteretep: it's generally acknowledged, in fact, that datatype contexts don't do much useful at all
08:11:04 --- join: tibbe (~tibbe@h158n3-vrr-d2.ias.bredband.telia.com) joined #haskell
08:11:11 --- join: Nisstyre-laptop (~yours@oftn/member/Nisstyre) joined #haskell
08:11:12 <peteretep> benmachine: I don't want to have to type out the classes for every function
08:11:13 <benmachine> peteretep: they are eventually going to be removed from the language
08:11:28 <benmachine> peteretep: well, there are a couple of GHC extensions that can help you out
08:11:45 <peteretep> I want to stick within the pure language
08:11:51 <peteretep> If the answer is "no", I guess that's ok :)
08:11:53 <benmachine> peteretep: just type them out then :P
08:11:56 --- join: atriq (~Taneb@host-78-146-168-239.as13285.net) joined #haskell
08:12:01 --- quit: herder (Remote host closed the connection)
08:12:02 <benmachine> peteretep: you *can* make the two constraints into one
08:12:09 <peteretep> benmachine: How so?
08:12:10 --- join: edwardk (~edwardk@pdpc/supporter/professional/edwardk) joined #haskell
08:12:38 <benmachine> peteretep: by doing something like, class ShowEq a; instance (Show a, Eq a) => ShowEq a
08:12:53 <benmachine> peteretep: actually, even that might require extensions, although fairly innocuous ones
08:13:12 <edwardk> rwbarton: hrmm the nicest version of a traversal/lens for json really wants me to go add a whole new concept to lens. thinking about how best to address embedding/projection pairs ;)
08:13:12 <peteretep> benmachine: I'll look in to that, thanks
08:13:16 <hpc> if you use sufficiently ridiculous extensions, you can even do something like
08:13:17 --- quit: ts33kr (Quit: Computer has gone to sleep.)
08:13:23 <hpc> ShowEq a = (Show a, Eq a)
08:13:26 <hpc> iirc
08:13:38 <benmachine> hpc: well, type ShowEq a = (Show a, Eq a)
08:13:39 <hpc> (probably "type ShowEq ..."
08:13:43 <hpc> yeah, that
08:13:44 <c_wraith> yes, the ConstraintKinds extension allows that
08:13:59 <benmachine> and GADTs would allow you to actually put the dictionary in the data type which is what you wanted
08:14:00 --- quit: copumpkin (Quit: Computer has gone to sleep.)
08:14:03 <benmachine> anyway.
08:14:23 <benmachine> insitu: I was helping you, I haven't forgotten; but in any case defining "helper" once you have the type is fairly easy
08:14:57 <insitu> benmachine: yes. no pb, just crawling my way into it. Think I got it tooo
08:15:13 <benmachine> insitu: cool :) without any recursion in helper, right?
08:15:26 <insitu> benmachine: of course !
08:15:35 <benmachine> insitu: awesome
08:15:50 <edwardk> peteretep: in general constraints on data types like that _never_ do what you want them to do. it merely ensures when you go to call the constructor that you know those properties hold. it _doesnt_ do the GADT thing and bundle them with the constructor, so you get no value out of it
08:15:54 <insitu> benmachine: wait a minute, still not done
08:16:09 <edwardk> peteretep: this is why we removed them from the language. they aren't in the current language standard
08:16:15 <peteretep> edwardk: Noted, thanks
08:16:21 <edwardk> they are a historical accident of how 'seq' used to work
08:16:37 <hpc> edwardk: ooh, do tell
08:16:41 <hpc> i haven't heard that story
08:16:58 <benmachine> edwardk: is this to do with strictness annotations in data types?
08:17:05 <edwardk> hpc: Seq used to require a class. its all in the history of haskell paper
08:17:20 <hpc> oh, that one; i have heard the story then
08:17:26 <edwardk> benmachine: exactly. to put a ! annotation in then you'd have to have to be able to derive an instance of Seq for the type of the field
08:18:16 <insitu> benmachine: ok got it
08:18:22 <benmachine> insitu: 'k
08:18:26 <edwardk> when they ripped that out of the language, the original motivation for those annotations died, and it only lived on in a couple of misguided constraints on Data.Complex, etc.
08:18:28 <insitu> filter' :: (a -> Bool) -> L a (Rec (L a)) -> Rec (L a)
08:18:28 <insitu> filter' p Nil                 = In Nil
08:18:28 <insitu> filter' p (L a l) | p a       = In (L a l)
08:18:28 <insitu>                   | otherwise = l
08:18:29 --- join: mikecb (~quassel@pool-108-28-251-107.washdc.fios.verizon.net) joined #haskell
08:18:31 <insitu>  
08:18:41 --- quit: choo (Read error: Connection reset by peer)
08:18:41 --- join: warrensomebody (~warrensom@c-24-5-88-248.hsd1.ca.comcast.net) joined #haskell
08:18:57 --- join: nalT (~NALT@cpc5-perr13-2-0-cust233.perr.cable.virginmedia.com) joined #haskell
08:19:02 --- join: choo (~choo@113.68.61.117) joined #haskell
08:19:02 <benmachine> insitu: yeah, that's pretty much what I got (although if you want to paste that much stuff, you should really use hpaste.org)
08:19:11 <benmachine> (not least because it gets lost otherwise)
08:19:33 --- quit: akamaus (Ping timeout: 252 seconds)
08:19:36 --- join: astropirate (~astropira@pool-96-241-226-191.washdc.fios.verizon.net) joined #haskell
08:20:01 --- quit: osa1 (Quit: Konversation terminated!)
08:20:03 <insitu> benmachine: you are definitely right. My irc etiquette is really lame
08:20:08 --- join: sanemat (~sanemat@7c2947bf.i-revonet.jp) joined #haskell
08:20:25 <insitu> benmachine: that's really enlightening!
08:20:39 <insitu> benmachine: looks like I got some epiphany today
08:20:41 <benmachine> insitu: yeah that was fun for me to do too :P
08:20:54 <benmachine> I've been vaguely aware of the technique but not actually used it as such
08:21:04 <benmachine> it's pretty neat
08:21:28 <insitu> benmachine: really. I am trying to understand this from a soft eng perspective
08:21:48 <insitu> benmachine: not sure it really helps write great software though
08:21:50 <benmachine> heh
08:21:58 <benmachine> maybe, maybe not
08:22:10 --- join: zeissoctopus (~zeissocto@183178133120.ctinets.com) joined #haskell
08:22:11 <benmachine> I'm a mathematician so I don't mind :)
08:22:35 <insitu> benmachine: anyway, thanks a lot for your help. Would not have groked it without.
08:22:39 <astropirate> i'm a magician
08:22:40 <benmachine> np
08:23:02 --- join: cinema (~cinema@dan75-1-89-82-138-66.dsl.sta.abo.bbox.fr) joined #haskell
08:23:05 <latro`a> all haskellers are magicians ;P
08:23:18 <insitu> benmachine: and you were right, I was confused by the 'a' type variable
08:23:24 <astropirate> heh i'm a newbie i'm afraid :(
08:23:29 --- quit: firstclassfunc (Changing host)
08:23:29 --- join: firstclassfunc (~hklein@unaffiliated/firstclassfunc) joined #haskell
08:23:31 <insitu> in cata's signature
08:23:31 --- join: sprang (~sprang@c-76-102-12-172.hsd1.ca.comcast.net) joined #haskell
08:23:38 <hpc> latro`a: except for Oleg; his code is cold hard mysticism
08:23:39 <hpc> :P
08:23:51 <timthelion> hpc: link?
08:24:01 <firstclassfunc> what is haskell's biggest perk?
08:24:12 <timthelion> firstclassfunc: lol
08:24:26 --- quit: insitu (Quit: ERC Version 5.3 (IRC client for Emacs))
```
